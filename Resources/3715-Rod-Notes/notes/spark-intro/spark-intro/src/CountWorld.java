import static spark.Spark.get;

public class CountWorld {
   // counter, not thread safe
   static int c = 0;

   public static void main(String[] args) {
   
      get("/count", (request, response) -> {
         c++;
         return "The count is " +  c;
      });

      get("/reset", (request, response) -> {
         c = 0;
         return "The count is reset to " +  c;
      });
   
   }
}
