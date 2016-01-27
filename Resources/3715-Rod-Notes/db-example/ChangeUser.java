import java.net.URL;
import java.net.URLConnection;
import java.net.HttpURLConnection;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Scanner;

public class ChangeUser {
    public static void main(String[] args) throws Exception {
        URL url = new URL("http://localhost:4567/db/user/" + args[0] );
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        conn.connect();

        PrintStream pr = new PrintStream(conn.getOutputStream());
        pr.print("{\"id\":\"q\",\"password\":\"FB\",\"name\":\"Foo Bar\",\"role\":\"user\"}");
        pr.flush();

        Scanner sc = new Scanner(conn.getInputStream());
        while ( sc.hasNextLine() ) {
            String line = sc.nextLine();
            System.out.println(line);
        }
        sc.close();
        pr.close();
    }
}
