import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.port;
import static spark.Spark.externalStaticFileLocation;

public class MathForm {
    public static void main(String[] args) {
        port(8090); // change to port to 8090
        externalStaticFileLocation("static/mathform");

        get("/add", (request, response) -> {
            String x = request.queryParams("x");
            String y = request.queryParams("y");
            return MathCalc.addPage(x, y);
        });

        get("/mult", (request, response) -> {
            String x = request.queryParams("x");
            String y = request.queryParams("y");
            return MathCalc.multPage(x, y);
        });
    }
}
