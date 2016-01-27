import static spark.Spark.get;
import static spark.Spark.put;
import static spark.Spark.post;
import static spark.Spark.delete;
import spark.ResponseTransformer;

import java.sql.SQLException;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

public class DbRest {
    public static void main(String[] args) throws Exception {
        UserAuthDBManger db = new UserAuthDBManger( "u.db", 5 );

        //@snipit DbRest.get
        // Retrieve a user
        get("/db/user/:user", "application/json", (request, response) -> {
            response.type("application/json");
            try {
                String uid = request.params(":user");
                UserAuthDBManger.User u = db.getUserById( uid );
                if ( u != null ) {
                    return u;
                }
                else {
                    return uid + " is unknown";
                }
            }
            catch( SQLException ex ) {
                return ex.getMessage();
            }
        }, new JsonTransformer() );
        //@snipit-end DbRest.get

        //@snipit DbRest.delete
        // Delete a user
        delete("/db/user/:user", "application/json", (request, response) -> {
            response.type("application/json");
            try {
                String uid = request.params(":user");
                UserAuthDBManger.User u = db.getUserById( uid );
                if ( u != null ) {
                    db.deleteUser(uid);
                    return u;
                }
                else {
                    return uid + " is unknown";
                }
            }
            catch( SQLException ex ) {
                return ex.getMessage();
            }
        }, new JsonTransformer() );
        //@snipit-end DbRest.delete

        //@snipit DbRest.post
        // Create a user
        post("/db/user/:user", "application/json", (request, response) -> {
            response.type("application/json");
            String uid = request.params(":user");
            Gson gson = new Gson();
            String b = request.body();
            UserAuthDBManger.User u = null;
            try {
                u = gson.fromJson(b, UserAuthDBManger.User.class);
            }
            catch( JsonParseException ex ) {
                return "not a valid json body";
            }
            try {
                db.addUser( u.id, u.password, u.name, u.role );
            }
            catch( SQLException ex ) {
                return ex.getMessage();
            }
            return u;
        }, new JsonTransformer() );
        //@snipit-end DbRest.post

        //@snipit DbRest.put
        // Update a user
        put("/db/user/:user", "application/json", (request, response) -> {
            response.type("application/json");
            String uid = request.params(":user");
            UserAuthDBManger.User u = db.getUserById( uid );
            if ( u == null ) {
                return uid + " is unknown";
            }
            Gson gson = new Gson();
            String b = request.body();
            UserAuthDBManger.User nu = null;
            try {
                nu = gson.fromJson(b, UserAuthDBManger.User.class);
            }
            catch( JsonParseException ex ) {
                return "not a valid json body";
            }
            try {
                if ( ! u.password.equals( nu.password ) ) {
                    db.changePassword( uid, nu.password );
                }
                if ( ! u.role.equals( nu.role ) ) {
                    db.changeRole( uid, nu.role );
                }
            }
            catch( SQLException ex ) {
                return ex.getMessage();
            }
            return nu;
        }, new JsonTransformer() );
        //@snipit-end DbRest.put
   }
}

class JsonTransformer implements ResponseTransformer {
    private Gson gson = new Gson();

    @Override
    public String render(Object model) {
        return gson.toJson(model);
    }
}
