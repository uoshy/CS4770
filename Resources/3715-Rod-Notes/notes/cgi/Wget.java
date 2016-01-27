import java.net.URLConnection;
import java.net.URL;
import java.io.InputStreamReader;

public class Wget {
    public static void main( String[] args ) throws Exception {
	URL url = new URL(args[0]);
	URLConnection conn = url.openConnection();
	conn.connect();
	InputStreamReader content =
	    new InputStreamReader( conn.getInputStream() );
	int ch;
	while( (ch=content.read()) != -1 ) {
	    System.out.print( (char)ch );
	}
    }
}
