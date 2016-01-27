import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.port;
import static spark.Spark.externalStaticFileLocation;
import spark.Session;

public class MathPage {
    private static final String ERROR_PAGE = 
        "<html>" +
        "<head><title>Math Calculations</title></head>" +
        "<body> <h1>Error</h1> %s </body>" +
        "</html>";

    private static String errorPage( String msg ) {
        return String.format( ERROR_PAGE, msg );
    }

    public static void main(String[] args) {
        port(8090); // change to port to 8090
        externalStaticFileLocation("static/mathpage");

        get("/style", (request, response) -> {
            Session sess = request.session(true);
            if ( sess == null ) {
                return errorPage("could not create session");
            }
            String style = request.queryParams("style");
            if ( style == null ) {
                return errorPage("no style attribute");
            }
            else {
                if ( style.equals("text") ) {
                    sess.attribute("presenter", new TextMathPresenter() );
                }
                else if ( style.equals("graphics") ) {
                    sess.attribute("presenter", new GraphicMathPresenter() );
                }
                else {
                    return errorPage("unknown style");
                }
            }
            response.redirect("/math-form.html");
            return null;
        });

        get("/add", (request, response) -> {
            MathPresenter presenter = request.session().attribute("presenter");
            if ( presenter == null ) {
                return errorPage("unknown presenter");
            }
            String x = request.queryParams("x");
            String y = request.queryParams("y");
            String res = presenter.parseParams(x, y );
            if ( res != null ) {
                return res;
            }
            return presenter.getAddPage();
        });

        get("/mult", (request, response) -> {
            MathPresenter presenter = request.session().attribute("presenter");
            if ( presenter == null ) {
                return errorPage("unknown presenter");
            }
            String x = request.queryParams("x");
            String y = request.queryParams("y");
            String res = presenter.parseParams(x, y );
            if ( res != null ) {
                return res;
            }
            return presenter.getMultPage();
        });
    }
}
