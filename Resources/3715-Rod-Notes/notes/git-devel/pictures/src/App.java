import static spark.Spark.port;
import static spark.Spark.get;
import static spark.Spark.externalStaticFileLocation;

import static java.lang.System.out;

public class App {
    public static void log( String msg ) {
        out.println( msg );
    }

    public static void main(String[] args) {
        port(8090); // change to port to 8090
        externalStaticFileLocation("static");

        get("/", (request, response) -> {
            response.redirect("/index.html");
            return null;
        } );

        // client editing
        Edit.sparkSetup();
        // back-in storage
        Store.sparkSetup();
    }
}
