import static spark.Spark.get;

public class Hello {
    public static void main(String[] args) {
   
        get("/hi", (request, response) -> {
            return
                "<html>\n" +
                "<body>\n" +
                "<h1>HI</h1>\n" +
                "</body>\n" +
                "</html>\n";
        });

        get("/hellos/:num", (request, response) -> {
            String head =
                "<html>\n" +
                "<body>\n";
            String tail =
                "</body>\n" +
                "</html>\n";

            // should be error checked XXX
            int n = Integer.parseInt( request.params(":num") );
            String body = "";
            for( int i = 0; i < n; i++ ) {
                body += "<h1>Hello</h1>\n";
            }
            return head + body + tail;
      });
   }
}
