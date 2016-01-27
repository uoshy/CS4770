import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.port;
import static spark.Spark.externalStaticFileLocation;
import static spark.Spark.halt;

import spark.ResponseTransformer;

import static java.lang.System.out;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

public class AjaxAddDemo {
    private static void log( String msg ) {
        out.println( msg );
    }

    public static void main(String[] args) {
        port(8090); // change to port to 8090
        externalStaticFileLocation("static");
   
        //@snipit form1
        get("/doAddGet/:v1/:v2", (request, response) -> {
            response.type("text/plain");
            String v1 = request.params(":v1");
            String v2 = request.params(":v2");
            if( v1 == null || v2 == null ) {
                log("/doAddGet: missing values");
                halt(400, "missing values");
            }
            try {
                int i1 = Integer.parseInt( v1 );
                int i2 = Integer.parseInt( v2 );
                int sum = i1 + i2;
                log(String.format("/doAddGet: %d + %d = %d", i1, i2, sum ));
                return String.valueOf( sum );
            }
            catch ( NumberFormatException ex ) {
                log("/doAddGet: malformed values");
                halt(400, "malformed values");
            }
            return null;
        });
        //@snipit-end form1

        //@snipit form2
        post("/doAddPost", (request, response) -> {
            response.type("text/plain");
            String v1 = request.queryParams("v1");
            String v2 = request.queryParams("v2");
            if( v1 == null || v2 == null ) {
                log("/doAddPost: missing values");
                log( "body:\n" + request.body() );
                halt(400, "missing values");
            }
            try {
                int i1 = Integer.parseInt( v1 );
                int i2 = Integer.parseInt( v2 );
                int sum = i1 + i2;
                log(String.format("/doAddPost: %d + %d = %d", i1, i2, sum ));
                return String.valueOf( sum );
            }
            catch ( NumberFormatException ex ) {
                log("/doAddGet: malformed values");
                halt(400, "malformed values");
            }
            return null;
        });
        //@snipit-end form2

        //@snipit form3
        // demonstrates receiving an object with JSON
        post("/doAddJSON", (request, response) -> {
            response.type("text/plain");
            try {
                Gson gson = new Gson();

                String b = request.body();
                // attempt to convert JSON to SumObject
                SumObject obj = gson.fromJson(b, SumObject.class);
                int sum = obj.v1 + obj.v2;
                log(String.format("/doAddJSON: %d + %d = %d",obj.v1, obj.v2, sum ));
                return String.valueOf( sum );
            }
            catch ( JsonParseException ex ) {
                log("/doAddGet: malformed values");
                halt(400, "malformed values");
            }
            return null;
        });
        //@snipit-end form3

        //@snipit json
        // demonstrates sending an object with JSON
        // test with: curl http://localhost:8090/json
        get("/json", "application/json", (request, response) -> {
            response.type("application/json");
            return new HelloObject();
        }, new JsonTransformer() );
        //@snipit-end json
    }
}

//@snipit SumObject
class SumObject {
    public int v1;
    public int v2;
}
//@snipit-end SumObject

//@snipit HelloObject
class HelloObject {
    private String hw = "hello";
    private int num = 47;
    private java.util.Date d = new java.util.Date();
}
//@snipit-end HelloObject

//@snipit JsonTransformer
class JsonTransformer implements ResponseTransformer {
    private Gson gson = new Gson();

    @Override
    public String render(Object model) {
        return gson.toJson(model);
    }
}
//@snipit-end JsonTransformer
