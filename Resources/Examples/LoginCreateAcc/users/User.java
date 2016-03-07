package users;

/**
 * A class to model a User object. Has a username and password.
 * On a later date add fields for other things required on registration such as
 * email, real name, address, etc. 
 *
 * May need to change depending on the requirements of the sql database.. I dont know.
 *
 * @author Alex Brandt
 * @version 2015.2.26 - initial set up
 */
public class User 
{
    /** Ther user's username */
    private String name;

    /** The user's password */
    private String password;

    /** The user's role. Normal or Admin */
    private String role;

    // number of games played
    private int gamesPlayed;

    // number of wins
    private int wins;

    // number of losses
    private int losses;

    // number of sequences in current game
    private int seqs;
    
    /** 
     * Construct a new User with a normal role
     * @param name the User's username
     * @param password the User's password
     */
    public User(String name, String password)
    {
        this(name, password, "Normal", 0, 0, 0);
    }

    /** 
     * Construct a new user with a name and password and role.
     *
     * @param name the User's username
     * @param password the User's password
     * @param role the User's role
     */
    public User(String name, String password, String role) 
    {
        this(name, password, role, 0, 0, 0);
    }

    public User(String name, String password, String role, int games, int wins, int losses) 
    {
        this.name = name;
        this.password = password;
        gamesPlayed = games;
        this.wins = wins;
        this.losses = losses;
        if(role.equals("Admin")) setAdmin(true);
        else setAdmin(false);
    }

    /**
     * Get the user's username. 
     * @return the User's username
     */
    public String getUserName()
    {
        return name;
    }

    /**
     * Get the user's password.
     * @return the User's password
     */
    public String getPassword()
    {
        return password;
    }

    public void setPassword(String newPassword) {
        password = newPassword;
    }

    /**
     * Get the user's role. Normal or Admin
     * @return the User's role
     */
    public String getRole()
    {
        return role;
    }

    // getters game stats
    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    // get/set/change number of sequences
    public void resetSeqs() {
        seqs = 0;
    }

    public int getSeqs() {
        return seqs;
    }

    public void addSeqs() {
        seqs++;
    }

    // methods to increase each stat by 1
    public void playGame() {
        gamesPlayed++;
    }

    public void winGame() {
        wins++;
    }

    public void loseGame() {
        losses++;
    }

    /**
     * Set the user's role to admin if input is true. Normal otherwise
     * @param yes the flag to determine if a user should be an Admin.
     */
    public void setAdmin(boolean yes)
    {
        if(yes) role = "Admin";
        else role = "Normal";
    }

    /**
     * Determine if this User is equal to another user.
     * @param obj the other user
     * @return true iff the users are the same
     */
    @Override
    public boolean equals(Object obj)
    {
        User u;
        if(obj instanceof User) 
        {
            u = (User) obj;
            if(u.name.equals(this.name) && u.password.equals(this.password))
                return true;
        }
        return false;
    }
   
    /**
     * A nice toString method which formats the User object for output
     * to the text file which stores all users. 
     *
     * @see Users#addUser
     */
    public String toString()
    {
        return name + "|" + password + "|" + role;
    }
}

