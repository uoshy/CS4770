import static spark.Spark.get;

public class CountSessionExample {
    public static void main(String[] args) {
        get("/count", (request, response) -> {
            Integer count = request.session().attribute("count");
            int c = 0;
            if (count != null) {
                c = (int)count;
            }
            c += 1;
            request.session().attribute("count", new Integer(c));
            return "The count for each session is " +  c;
        });

        get("/reset", (request, response) -> {
            request.session().attribute("count", new Integer(0));
            return "The count for each session reset"; 
        });

    }
}
