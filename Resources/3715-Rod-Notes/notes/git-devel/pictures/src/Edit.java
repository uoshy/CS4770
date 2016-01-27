import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.halt;

import static java.nio.file.Files.readAllBytes;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.io.IOException;

public class Edit {
    private static String STORE_PATH = "./store/";
    private static String EDIT_TEMPLATE = "./templates/circles.html";
    private static String EDIT_PAGE = "";

    public static void sparkSetup() {
        try {
            EDIT_PAGE = new String(readAllBytes(Paths.get(EDIT_TEMPLATE)));
        }
        catch( IOException ex ) {
            System.out.println("no templates");
            System.exit( 1 );
        }

        get("/pictures/edit/:user/:pic", (request, response) -> {
             String user = request.params("user");
             String pic = request.params("pic");
             if (  user == null || pic == null ) {
                 App.log("pics-dit missing values");
                 halt(400, "missing values");
             }
             App.log("handeling: /pictures/edit");
             response.type("text/html");
             try {
                 Path p = Paths.get(STORE_PATH, user, pic );
                 String source = new String(readAllBytes(p));
                 return String.format(EDIT_PAGE, source);
             }
             catch( IOException ex ) {
                 response.redirect("/error.html");
                 return null;
             }
        } );
    }
}
