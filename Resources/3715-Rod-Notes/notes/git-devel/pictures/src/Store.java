import static spark.Spark.get;
import static spark.Spark.put;
import static spark.Spark.halt;

import java.io.File;
import static java.nio.file.Files.readAllBytes;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class Store {

    private static final String A_LINK = 
        "<a href='/pictures/edit/%s/%s'>%s</a>\n";
    private static final String STORE_PATH = "./store";
    private static final String LIST_PAGE = 
        "<!DOCTYPE html>\n" +
        "<html>\n" +
        "<head>\n" +
        "<title>Pictures Listing Page</title>\n" +
        "<meta charset='UTF-8'>\n" +
        "<link rel='stylesheet' type='text/css' href='/css/app.css'>\n" + 
        "</head>\n" +
        "<body>\n" +
        "<h1>Users</h1>\n" +
        "%s\n" +
        "</body>\n" +
        "</html>\n";

    public static void sparkSetup() {
        // list the pictures belonged to a user
        get("/pictures/:user", (request, response) -> {
             String user = request.params("user");
             if ( user == null ) {
                 App.log("store list: missing values");
                 halt(400, "missing values");
             }
             App.log("handeling: /pictures/" + user);
             File dir = new File( STORE_PATH + "/" + user );
             if( !dir.isDirectory() ) {
                  response.redirect("/error.html");
                  return null;
             }
             String[] list = dir.list();
             StringBuilder sb = new StringBuilder();
             sb.append("<ul>\n");
             for ( String n : list ) {
                 sb.append("<li>\n");
                 String a = String.format(A_LINK, user, n, n );
                 sb.append( a );
                 sb.append("</li>\n");
             }
             sb.append("</ul>\n");
             return String.format( LIST_PAGE, sb.toString() );
        } );

        get("/pictures/:user/:pic", (request, response) -> {
            String user = request.params("user");
            String pic = request.params("pic");
            if ( user == null || pic == null ) {
               App.log("pictures get missing values");
               halt(400, "missing values");
            }
            response.type("image/svg+xml");
            try {
                Path p = Paths.get(STORE_PATH, user, pic );
                String source = new String(readAllBytes(p));
                return source;
            }
            catch( IOException ex ) {
                response.redirect("/error.html");
                return null;
            }
        } );

        put("/pictures/:user/:pic", (request, response) -> {
            String user = request.params("user");
            String pic = request.params("pic");
            if ( user == null || pic == null ) {
               App.log("pictures get missing values");
               return "missing values";
            }
            try {
                Path p = Paths.get(STORE_PATH, user, pic );
                BufferedWriter wt =
                    new BufferedWriter( new FileWriter( p.toString() ));
                String b = request.body();
                App.log( b );
                wt.write( b );
                wt.close();
                return "ok";
            }
            catch( IOException ex ) {
                return "failed";
            }
        } );
    }
}
