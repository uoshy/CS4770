import static spark.Spark.externalStaticFileLocation;
import static spark.Spark.get;

public class All {
    // counter, not thread safe
    static int c = 0;

    public static void main(String[] args) {

        // path to files
        externalStaticFileLocation("static");

        get("/count", (request, response) -> {
            c++;
            return "The count is " +  c;
        });

        get("/sess", (request, response) -> {
            Integer count = request.session().attribute("count");
            int c = 0;
            if (count != null) {
                c = (int)count;
            }
            c += 1;
            request.session().attribute("count", new Integer(c));
            return "Session count is " +  c;
        });
    }
}
