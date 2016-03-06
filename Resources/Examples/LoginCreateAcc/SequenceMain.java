import spark.Session;
import spark.Request;
import spark.Response;
import spark.ResponseTransformer;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.before;
import static spark.Spark.halt;
import static spark.Spark.externalStaticFileLocation;

import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.LinkedBlockingQueue;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import users.User;
import users.Users;

import cards.Card;

import jsonObjects.*;

import java.sql.SQLException;

/**
 * The main class to run the java spark framework.
 */
public class SequenceMain 
{
    /* private static Users theUsers; //see src/users/Users.java */
    private static Collection<User> users;
    private static ArrayList<BoardGame> games; 
    private static ArrayList<BoardGame> endedGames;

    /* The player waiting to connect */
    private static User playerWaiting;

    /** Database manager */
    private static UserAuthDBManager userManager;

    /** Template files */
    private static final String ACCSETTINGS_TEMPLATE = "accountSettings.mustache";
    private static final String LEADERBOARD_TEMPLATE = "leaderboard.mustache";
    private static final String ADMIN_TEMPLATE = "admin.mustache";

    /** Erors returned by get methods to be caught in clietnside javascript*/
    private static final int ALREADY_IN_GAME_ERROR = -404;
    private static final int ALREADY_WAITING_ERROR = -405;

    /** Default password for admin settings password reset */
    private static final String DEFAULT_PASSWORD = "!)@(#*$&%^";

    //get the users from file. Will eventually replace with database stuff
    private static void setup()
    {
        // read all users from database and store in a Collection
        try {
            userManager = new UserAuthDBManager("users.db", 5);
            users = userManager.getAllUsers();
        } catch (SQLException ex) {
            log(ex.getMessage());
        }
        //initialize BoardGame collections
        playerWaiting = null;
        games = new ArrayList<BoardGame>(); 
        endedGames = new ArrayList<BoardGame>();
    }

/**
 * A set of static helper methods used by the server. 
 *
 */

    // for printing server log, currently prints it to System.out
    public static void log(String src) {
        System.out.println(src);
    }

    //determine if a certain user exists in a BoardGame instance.
    public static boolean checkIfInGame(User u)
    {
        for(BoardGame b : games)
            if(b.hasPlayer(u))
                return true;
        return false;
    }

    //run the spark framework
    public static void main(String[] args) throws SQLException 
    {
        setup(); //get the users on server start up
        externalStaticFileLocation("static"); 
        MustacheTemplateEngine mte = new MustacheTemplateEngine("templates");
        userManager = new UserAuthDBManager("users.db", 5);

        //redirect root to index.html
        get("/", (request, response) -> 
                {
                    response.redirect("/index.html");
                    return null;   
                });

        //redirect /index to /index.html
        get("/index", (request, response) -> 
                {
                    response.redirect("/index.html");
                    return null;   
                });

/**
 * Register/Login/Account settings
 *
 */
        //make sure you are logged in before anything
        before("/account.html", (request, response) ->
        {       
            User user = request.session().attribute("user");
            if(user == null) 
            {
                response.redirect("/login.html");
                return;
            }
        });

        //Posting the form (js really) on login.html redirects to account.html
        //if successful. Otherwise send back an error message.
        post("/login.html", (request, response) ->
        {
            response.type("plain/text");
            String name;
            String password;
            try
            {
                Gson gson = new Gson();
                String body = request.body();
                LoginRequest lr = gson.fromJson(body, LoginRequest.class);
                name = lr.username;
                password = lr.password;
            }
            catch (JsonParseException ex)
            {
                log("Malformed values in login request");
                return "Error logging in. Please try again.";
            }
            if(name == null || password == null)
            {
                return "Error logging in. Please try again.";
            }
            User u = new User(name, password);
            for(User user : users)
            {   
                if(u.equals(user))
                { // successful login
                    Session session = request.session(true);
                    session.attribute("user", user);
                    response.redirect("/account.html");
                    return null;
                }
            }
            // user doesn't exist or passwords don't match 
            log("Login failed");
            return "Error logging in. Please try again.";
        });

        // allow user to log out
        get("logout", (request, response) -> {
            Session sess = request.session(true);
            if(sess.attribute("user") != null) {
                sess.attribute("user", null);
                response.redirect("/index.html");
            } else {
                // just redirect to the same page if user wasn't logged in -
                // this needs to be changed so that the "Log out" link isn't
                // even shown when the user is logged in
                response.redirect(request.url());
            }
            return null;

        });

        // used to check if username exists with AJAX during registration
        get("checkUser/:username", (request, response) -> {
            response.type("text/plain");
            String username = request.params(":username");
            users = userManager.getAllUsers();
            for(User u : users) {
                if(u.getUserName().toLowerCase().equals(username))
                    return "exists";
            }
            return "does not exist";
        });

        // posting the form on register.html redirects to account.html on a
        // successful registration 
        post("register.html", (request, response) -> {
            String username = request.queryParams("username");
            String password1 = request.queryParams("password1");
            String password2 = request.queryParams("password2");

            //checked on clientside with get(checkUser/:username)
            //shouldn't happen
            for(User u : users) {
                if(u.getUserName().equalsIgnoreCase(username)) {
                    // username already exists, registering failed
                    // (this should be prevented by javascript/AJAX on the
                    // register page anyway, but just in case...)
                    log("username exists, register failed");
                    response.redirect("/register.html");
                    return null;
                }
            }

            //checked on clientside. Shouldn't happen
            if(!password1.equals(password2)) {
                // passwords don't match
                log("passwords don't match, register failed");
                response.redirect("/register.html");
                return null;
            }

            // everything is fine, create new user in the DB and add a new User
            // object to the users Collection
            User user = new User(username, password1);
            users.add(user);
            userManager.addUser(username, password1, user.getRole());
            // log in the user and redirect to account settings
            Session session = request.session(true);
            session.attribute("user", user);
            response.redirect("/account.html");
            return null;
        });

        //get the account page after a valid login. Else redirect to /login.html
        get("/account.html", (request, response) -> {
            User u = request.session().attribute("user");
            String user = null;
            if(u != null) user = u.getUserName();
            Map map = new HashMap();
            map.put("username", user);
            if(u != null) 
                if(u.getRole().equals("Admin"))
                    map.put("adminDisplay", "block");
            else 
                map.put("adminDisplay", "none");
            return new ModelAndView(map, ACCSETTINGS_TEMPLATE);           
        }, mte);
    
        //check if user's password matches the entered password.
        get("checkPassword/:password", (request, response) ->
        {
            User user = request.session().attribute("user");
            users = userManager.getAllUsers();
            if(user == null) 
            {
                response.redirect("/login.html");
                return null;
            }
            response.type("text/plain");
            String password = request.params(":password");
            for(User u : users) 
            {
                if(u.equals(user))
                {
                    if(u.getPassword().equals(password))
                       return "exists";
                }
            }
            return "does not exist";
        });

        // changing username and password, aka "account settings"
        post("/account.html", (request, response) -> 
        {
            User user = request.session().attribute("user");
            String username = user.getUserName();
            if(user == null) 
            {
                response.redirect("/login.html");
                return null;
            }
            String password1 = request.queryParams("password1");
            String password2 = request.queryParams("password2");
            String password = request.queryParams("currentPassword");
            if(password.equals(user.getPassword())) {
                log("updating password");
                // update password in database
                if(password1.equals(password2)) { // is checked in client, but just in case...
                    userManager.changePassword(username, password1);
                }
                // update password in User object
                for(User u : users) {
                    if (u.equals(user)) u.setPassword(password1);
                }

            }
            response.redirect("/account.html");
            return null;

        });

/**
 * Leaderboard paths
 *
 */
	    // only logged in users can view leaderboard
        before("/leader.html", (request, response) ->
        {       
            User user = request.session().attribute("user");
            if(user == null) 
            {
                response.redirect("/login.html");
                return;
            }
        });

        // get the leaderboard page
        get("/leader.html", (request, response) -> 
        {
            Collection<User> dbUsers = null;
            try {
                dbUsers = userManager.getAllUsers();
            } catch(SQLException ex) {
                log(ex.getMessage());
                log("reading users from DB failed while creating leaderboard");
                return new ModelAndView(new HashMap(), LEADERBOARD_TEMPLATE);
            }
            if(dbUsers != null) {
                Object[] allUsers = dbUsers.toArray();
                HashMap[] userData = new HashMap[allUsers.length];
                for(int i = 0; i < allUsers.length; i++) { 
                    User u = (User)allUsers[i];
                    HashMap map = new HashMap(); 
                    map.put("userName", u.getUserName()); 
                    map.put("gamesPlayed", u.getGamesPlayed());
                    map.put("wins", u.getWins());
                    map.put("losses", u.getLosses());
                    userData[i] = map;
                } 

                Map data = new HashMap();
                data.put("totalUsers", allUsers.length);
                data.put("userData", userData);
                return new ModelAndView(data, LEADERBOARD_TEMPLATE);
            } else {
                log("reading users from DB failed while creating leaderboard");
                return new ModelAndView(new HashMap(), LEADERBOARD_TEMPLATE);
            }
        }, mte);

/**
 * All game playing request paths. GameID is the unique game ID constantly 
 * sent by the client to identify which game they are in.
 */

        //serve the play game page. 
        before("play.html", (request, response) ->
        {
            User user = request.session().attribute("user");
            if(user == null) 
            {
                response.redirect("/login.html");
                return;
            }
        });

        //ensure logged in
        before("play/*", (request, response) ->
        {
            User user = request.session().attribute("user");
            if(user == null) 
            {
                response.redirect("/login.html");
                return;
            }
        });

        //try to create a new game instance. Wait in line if no opponent waiting
        //to connect with
        get("play/newGame", (request, response) ->
        {
            response.type("application/json");
            User requestUser = request.session().attribute("user");
            if(requestUser == null)
            { 
                response.redirect("/login.html");
                return null;
            }

            if(requestUser.equals(playerWaiting)) //if already waiting to connect
            {
                return new GameRequest(ALREADY_WAITING_ERROR);
            }    

            //cannot be in two games at once. 
            if(checkIfInGame(requestUser)) //if already in a game
            {
                return new GameRequest(ALREADY_IN_GAME_ERROR);
            }

            log("play/newGame: " + requestUser.getUserName());

            if(playerWaiting == null) //wait for another user to request a game
            {
                log("PlayerWaiting = null returning -1");
                playerWaiting = requestUser;
                return new GameRequest(-1);
            }
            else if(!playerWaiting.equals(requestUser)) //start a new game
            {
                log("New BoardGame! " + 
                        playerWaiting.getUserName() + " against " + 
                        requestUser.getUserName());
                BoardGame newGame = new BoardGame(playerWaiting, requestUser);
                games.add(newGame);
                playerWaiting = null;
                return new GameRequest(newGame.getSession());
            }
            else 
                return new GameRequest(-1);
        }, new JsonTransformer() );

        //called when a player tries to play a new game but they already exist in
        //a game on the server. Populates their screen with the current played game.
        get("play/gameRefresh", (request, response) ->
        {   
            User requestUser = request.session().attribute("user");
            //not logged in.
            if(requestUser == null)
            {
                response.redirect("/login.html");
                return null;
            }
            for(BoardGame b : games)
            {
                if(b.hasPlayer(requestUser)) {
                    if(b.getPlayerWithTurn().equals(requestUser))
                        return new GameRefresh(b.getSession(),
                                 b.getPlayerNumber(requestUser), b.getBoard(), true);
                    else
                        return new GameRefresh(b.getSession(), 
                                 b.getPlayerNumber(requestUser), b.getBoard(), false);

                }
            }
            return null;

        }, new JsonTransformer() );

        //polling request to determine if a game has been started for the user waiting 
        //to connect to a new game
        get("play/hasOpponent", (request, response) ->
        {
            response.type("application/json");
            User requestUser = request.session().attribute("user");
            if(requestUser == null)
            { 
                response.redirect("/login.html");
                return null;
            }
            log("play/hasOpponent: " + requestUser.getUserName());
            for(BoardGame b : games)
            {
                log("Board game session: " + b.getSession());
                if(b.hasPlayer(requestUser))
                    return new GameRequest(b.getSession()); //if found return ID
            }
            return new GameRequest(-1); //still waiting
        }, new JsonTransformer() );

        // cancels the game request made by the user if an opponent hasn't been
        // found after a specified time
        post("play/cancelNewGame", (request, response) -> {
            response.type("text/plain");
            User requestUser = request.session().attribute("user");
            if(requestUser == null)
            { 
                response.redirect("/login.html");
                return null;
            }
            log("play/cancelNewGame: " + requestUser.getUserName());
       
            if(playerWaiting.equals(requestUser)) {
                // the player is no longer waiting to be matched
                playerWaiting = null;
                // let client know that cancellation succeeded
                return "OK";
            }
            return "ERROR";
        });


        //Polling request to determine if the game has changed to be the player's turn
        post("play/isMyTurn", (request, response) ->
        {
            response.type("application/json");
            try
            {
                User requestUser = request.session().attribute("user");
                //not logged in. Shouldn't happen...
                if(requestUser == null)
                { 
                    response.redirect("/login.html");
                    return null;
                }
                Gson gson = new Gson();
                String body = request.body();
                GameID gid = gson.fromJson(body, GameID.class);

                boolean myTurn = false;

                for(BoardGame b : games)
                {
                    if(b.getSession() == gid.sessionID) 
                    {
                        if(requestUser.equals(b.getPlayerWithTurn()))
                            myTurn = true; //it is their turn
                    }
                }
                for(BoardGame b : endedGames) //let the losing person get the update
                {
                    if(b.getSession() == gid.sessionID)
                        myTurn = true; //it is their turn but the game is over
                    //this sitiuation is handeled by getBoardChange called by client next
                }
                return new TurnRequest(myTurn);
            }
            catch (JsonParseException ex)
            {
                log("Malformed values in getting isMyTurn");
                halt(400, "malformed values");
                return null;
            }

        }, new JsonTransformer() );

        /** 
         * Used during development. Implemented on client side. 
         *

        post("play/checkTimeout", (request, response) ->
        {
            try
            {
                User requestUser = request.session().attribute("user");
                //not logged in. Shouldn't happen EVER...
                if(requestUser == null)
                { 
                    response.redirect("/login.html");
                    return null;
                }

                Gson gson = new Gson();
                String body = request.body();
                GameID gid = gson.fromJson(body, GameID.class);

                for(BoardGame b : games)
                {
                    if(b.getSession() == gid.sessionID)
                        return new TimeoutCheck(false); //game has not timed out
                }
                return new TimeoutCheck(true);
            }
            catch (JsonParseException ex)
            {
                log("Malformed values in doing timeout");
                halt(400, "malformed values");
                return null;
            }
        }, new JsonTransformer() );
        */

        //timeout a game waiting for too long. Clientside does the timing
        post("/play/timeout", (request, response) ->
        {
            try
            {
                User requestUser = request.session().attribute("user");
                if(requestUser == null)
                { 
                    response.redirect("/login.html");
                    return null;
                }

                Gson gson = new Gson();
                String body = request.body();
                GameID gid = gson.fromJson(body, GameID.class);

                for(int i = 0; i < games.size(); i++)
                {   //remove the game from the list of games
                    if(games.get(i).getSession() == gid.sessionID) 
                        endedGames.add(games.remove(i)); 
                }                    

                return "";

            }
            catch (JsonParseException ex)
            {
                log("Malformed values in doing timeout");
                halt(400, "malformed values");
                return null;
            }
        });

        //Ends a game between two players. 
        post("/play/endGame", (request, response) -> {
            try
            {
                User requestUser = request.session().attribute("user");
                if(requestUser == null)
                { 
                    response.redirect("/login.html");
                    return null;
                }

                Gson gson = new Gson();
                String body = request.body();
                GameID gid = gson.fromJson(body, GameID.class);

                for(int i = 0; i < games.size(); i++)
                {   //remove the game from the list of games
                    if(games.get(i).getSession() == gid.sessionID) 
                        endedGames.add(games.remove(i));
                }                    

                return "";

            }
            catch (JsonParseException ex)
            {
                log("Malformed values in ending game");
                halt(400, "malformed values");
                return null;
            }
        });

        //get the player's hand for the specified game instance
        post("play/getHand", (request, response) -> {
            response.type("application/json");
            try {
                User requestUser = request.session().attribute("user");
                //not logged in. Shouldn't happen...
                if(requestUser == null)
                { 
                    response.redirect("/login.html");
                    return null;
                }
                Gson gson = new Gson();
                String body = request.body();
                GameID gid = gson.fromJson(body, GameID.class);
                for(BoardGame b : games)
                {
                    if(b.getSession() == gid.sessionID) 
                    {
                        ArrayList<Card> hand = b.getHandByNumber(b.getPlayerNumber(requestUser));
                        return new Hand(hand);
                    }
                }
                return null;
            }
            catch (JsonParseException ex)
            {
                log("Malformed values in getting hand");
                halt(400, "malformed values");
                return null;
            }
        }, new JsonTransformer()); 


        //get the BoardChange when the player gets possession of their turn
        //gets the opponent's move and if any sequences.
        post("play/getBoardChange", (request, response) ->
        {
            try 
            {
                User requestUser = request.session().attribute("user");
                //not logged in. Shouldn't happen...
                if(requestUser == null)
                { 
                    response.redirect("/login.html");
                    return null;
                }

                Gson gson = new Gson();
                String body = request.body();
                GameID gid = gson.fromJson(body, GameID.class);
                BoardGame tempGame = null;
                for(BoardGame b : games)
                {
                    if(b.getSession() == gid.sessionID) 
                    {
                        int player = b.getPlayerNumber(requestUser);
                        int winner = b.getWinner();
                        int[][] coords = b.getCoords();
                        if(winner != 0 && winner != player)
                            return new BoardObject(b.getLatestPlacedI(), 
                                    b.getLatestPlacedJ(), true, false, coords);
                        else
                            return new BoardObject(b.getLatestPlacedI(), 
                                    b.getLatestPlacedJ(), false, false, coords);
                    }
                }
                //now if your game is not found search through the ended games.
                for(int i = 0; i < endedGames.size(); i++)
                {
                    BoardGame b = endedGames.get(i);
                    if(b.getSession() == gid.sessionID)
                    {
                        endedGames.remove(i);
                        int player = b.getPlayerNumber(requestUser);
                        int winner = b.getWinner();
                        int[][] coords = b.getCoords();
                        if(winner == 0) // nobody won, opponent clicked "end game"
                            return new BoardObject(b.getLatestPlacedI(), 
                                            b.getLatestPlacedJ(), false, true, coords);
                        else // opponent won
                            return new BoardObject(b.getLatestPlacedI(), 
                                            b.getLatestPlacedJ(), true, false, coords);
                    }
                }
                return null; //not found anywhere
            }
            catch (JsonParseException ex)
            {
                log("Malformed values in getting board change");
                halt(400, "malformed values");
                return null;
            }
        }, new JsonTransformer() );


        //Path called to end a turn in the game. Adds a move to the BoardGame
        //instance and checks for winners. Draws a new card for the player
        post("play/endTurn", (request, response) ->
        {
            try 
            {
                User requestUser = request.session().attribute("user");
                //not logged in. Shouldn't happen...
                if(requestUser == null)
                { 
                    response.redirect("/login.html");
                    return null;
                }

                Gson gson = new Gson();
                String body = request.body();
                TurnPlay tp = gson.fromJson(body, TurnPlay.class);
                BoardGame tempGame = null;
                for(BoardGame b : games)
                {
                    if(b.getSession() == tp.sessionID) 
                        tempGame = b;
                }
                if(tempGame == null)//game was removed from collection somehow
                {          
                    return new MoveResponse(0, null, true);
                }       
                
                int player = tempGame.getPlayerNumber(requestUser);
                if(player <= 0) return null; //should definitely never happen
                // check how many sequences the user had before this move
                int sequences = requestUser.getSeqs();
                if(tp.remove) // remove card was played
                    tempGame.addRemoveTile(tp.i, tp.j, 0); // removes token from [i,j]
                else
                    tempGame.addRemoveTile(tp.i, tp.j, player);
                tempGame.drawNewCard(tp.handIndex, player);
                // give turn to other player
                tempGame.nextPlayerGetsTurn();

                int winner = tempGame.checkWinner();
                // coordinates of a newly completed sequence, or negative values
                // if this turn didn't result in a new sequence
                int[][] coords = tempGame.getCoords();

                // if a winner was found, remove the game from the games
                // collection (game has ended)
                if(winner > 0) {
                    User opponent = null;
                    if (player==1){ opponent = tempGame.getPlayerByNumber(2); }
                    else { opponent = tempGame.getPlayerByNumber(1); }
                    requestUser.playGame();
                    opponent.playGame();
                    if (winner == player){ requestUser.winGame(); opponent.loseGame(); }
                    else{ requestUser.loseGame(); opponent.winGame(); }
                    userManager.changeGamesPlayed(requestUser.getUserName(), requestUser.getGamesPlayed());
                    userManager.changeWins(requestUser.getUserName(), requestUser.getWins());
                    userManager.changeLosses(requestUser.getUserName(), requestUser.getLosses());
                    userManager.changeGamesPlayed(opponent.getUserName(), opponent.getGamesPlayed());
                    userManager.changeWins(opponent.getUserName(), opponent.getWins());
                    userManager.changeLosses(opponent.getUserName(), opponent.getLosses());
                    games.remove(tempGame);
                    endedGames.add(tempGame);
                }

                // respond with the player number that won (0 = nobody won yet)
                // and the coordinates of the possible new sequence
                return new MoveResponse(winner, coords, false);
            }
            catch (JsonParseException ex)
            {
                log("Malformed values in ending turn");
                halt(400, "malformed values");
                return null;
            }
        }, new JsonTransformer() );



/**
 *  ADMIN SETTINGS HERE
 */

        //only admins can see the admin page
        before("/admin/*", (request, response) -> 
        {
            User user = request.session().attribute("user");
            if ( user == null ) {
                response.redirect("/login.html");
                return;
            }
            if ( !user.getRole().equals( "Admin" ) ) {
                response.redirect("/account.html");
                return;
            }
        });

        // get the admin page
        get("/admin/admin.html", (request, response) -> 
        {
            try {
                users = userManager.getAllUsers();
            } catch (SQLException ex) {
                log(ex.getMessage());
            }
            Object[] allUsers = users.toArray();
            HashMap[] userData = new HashMap[allUsers.length];
            for(int i = 0; i < allUsers.length; i++) { 
                User u = (User)allUsers[i];
                HashMap map = new HashMap(); 
                map.put("userName", u.getUserName()); 
                map.put("password", u.getPassword());
                map.put("role", u.getRole());
                map.put("gamesPlayed", u.getGamesPlayed());
                map.put("wins", u.getWins());
                map.put("losses", u.getLosses());
                userData[i] = map;
            } 

            Map data = new HashMap();
            data.put("totalUsers", allUsers.length);
            data.put("userData", userData);
            return new ModelAndView(data, ADMIN_TEMPLATE);
        }, mte);

        //remove a user from the database
        get("/admin/remove/:user", (request, response) ->
        {
            String username = request.params(":user");
            userManager.deleteUser(username);
            response.redirect("/admin/admin.html");
            return null;
        });

        //reset a user's password to a default password
        get("/admin/resetPassword/:user", (request, response) ->
        {
            String username = request.params(":user");
            userManager.changePassword(username, DEFAULT_PASSWORD);
            response.redirect("/admin/admin.html");
            return null;
        });

        //make a user's role "Admin"
        get("/admin/setrole/admin/:user", (request, response) ->
        {
            String username = request.params(":user");
            userManager.changeRole(username, "Admin");
            response.redirect("/admin/admin.html");
            return null;
        });

        //make a user's role "Normal"
        get("/admin/setrole/normal/:user", (request, response) ->
        {
            String username = request.params(":user");
            userManager.changeRole(username, "Normal");
            response.redirect("/admin/admin.html");
            return null;
        });

        //reset user statistics
        get("/admin/resetStats/:user", (request, response) ->
        {
            String username = request.params(":user");
            userManager.changeGamesPlayed(username, 0);
            userManager.changeWins(username, 0);
            userManager.changeLosses(username, 0);
            response.redirect("/admin/admin.html");
            return null;

        });

    }//main

}
