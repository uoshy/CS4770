import static spark.Spark.get;
import spark.Route;
import spark.Request;
import spark.Response;

public class CountWorldInterface {
    // counter, not thread safe
    static int c = 0;

    public static void main(String[] args) {

        get("/count", new Route() {
            public Object handle(Request request, Response response)
            throws Exception
            {
                c++;
                return "The count-interface is " +  c;
            }
        });

        get("/reset", (request, response) -> {
            c = 0;
            return "The count is reset to " +  c;
        });

    }
}
