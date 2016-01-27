import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.before;
import static spark.Spark.port;
import static spark.Spark.externalStaticFileLocation;
import spark.Session;

public class Login {
    public static void main(String[] args) {

        port(8090); // change to port to 8090
        externalStaticFileLocation("static/login");

        before("/auth/*", (request, response) -> {
            String user = request.session().attribute("user");
            if ( user == null ) {
                response.redirect("/not-signed-in.html");
            }
        } );

        post("/login", (request, response) -> {
            String u = request.queryParams("user");
            String pw = request.queryParams("password");

            if ( u == null || pw == null ) {
                response.redirect("/error.html");
            }
            if ( u.equals("foo") && pw.equals("bar") ) {
                Session sess = request.session(true);
                if ( sess == null ) {
                    response.redirect("/error.html");
                }
                sess.attribute("user", u);
            }
            response.redirect("/auth/main.html");
            return null;
        });
    }
}
