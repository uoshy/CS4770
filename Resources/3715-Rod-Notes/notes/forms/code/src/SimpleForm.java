import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.port;

public class SimpleForm {
    private static final String FORM_PAGE = 
        "<html>\n" + 
        "<body>\n" + 
        "<form action='/form' method='POST'>\n" +
        "<label>text1 <input type='text' name='text1'></label>\n" +
        "<label>on <input type='radio' name='onoff' value='on' checked></label>\n" +
        "<label>off <input type='radio' name='onoff' value='off'></label>\n" +
        "<input type='submit' name='type' value='html'>\n" +
        "<input type='submit' name='type' value='plain'>\n" +
        "</body>\n" + 
        "</html>\n";

    private static final String SUBMIT_PAGE = 
        "<html>\n" + 
        "<body>\n" + 
        "<p>\n" +
        "text1 = (%s)<br>\n" +
        "onoff = (%s)<br>\n" +
        "<a href='/form'>Return to form</a>\n" +
        "</p>\n" +
        "</body>\n" + 
        "</html>\n";

    public static void main(String[] args) {
        port(8090); // change to port to 8090

        get("/form", (request, response) -> FORM_PAGE );

        post("/form", (request, response) -> {
            String type = request.queryParams("type");
            if ( type.equals("plain") ) {
                response.type("text/plain"); // change default
            }
            String t1 = request.queryParams("text1");
            String t2 = request.queryParams("onoff");
            return String.format( SUBMIT_PAGE, t1, t2 );
        });
    }
}
