import java.util.Scanner;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

public class GetUser {
    public static void main( String[] args ) throws Exception {
        URL url = new URL("http://localhost:4567/db/user/" + args[0]);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("GET");

        conn.connect();

        int resp=conn.getResponseCode();
        Scanner sc = new Scanner( conn.getInputStream(), "UTF-8");
        StringBuilder sb = new StringBuilder();
        while( sc.hasNextLine() ) {
            sb.append( sc.nextLine() );
            sb.append( '\n' );
        }
        sc.close();
        // or you can use the following to read the input
        // String text = sc.useDelimiter("\\A").next();
        // this is a bit of a hack

        Gson gson = new Gson();
        User u = gson.fromJson( sb.toString(), User.class);
        System.out.println( u );
    }

    public static class User {
        public final String id;
        public final String password;
        public final String name;
        public final String role;

        public User( String id, String password, String name, String role ) {
            this.id = id;
            this.password = password;
            this.name = name;
            this.role = role;
        }

        public String toString() {
            return id + " " + password + " " + name + " " + role;
        }
    }
}
