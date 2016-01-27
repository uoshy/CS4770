import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;

public class HttpRequestViewer {

    public static void sendNotFound( PrintStream ps ) {
        ps.println("HTTP/1.0 404 Not Found");
        ps.println("Connection: close");
        ps.println(); // end of header
        ps.flush();
    }

    public static void sendReply( String path, PrintStream ps ) {
        ps.println("HTTP/1.0 200 OK");
        ps.println("Connection: close");
        ps.println("Content-Type: text/html; charset=utf-8");
        ps.println(); // end of header
        ps.println("<html>");
        ps.println("<body>");
        ps.println("<pre>" + "path: " + path + "</pre>");
        ps.println("<pre>" + new Date() + "</pre>");
        ps.println("</body>");
        ps.println("</html>");
        ps.flush();
    }

    public static void main( String[] args ) {
        try {
            int port = Integer.parseInt( args[0] );
            ServerSocket listen = new ServerSocket( port );
            while ( true ) {
                Socket sock = listen.accept();
                Scanner sc = new Scanner( sock.getInputStream() );
                PrintStream ps = new PrintStream(sock.getOutputStream(),false);
                // get request
                if ( !sc.hasNextLine() ) {
                    ps.close();
                    sc.close();
                    sock.close();
                    continue;
                }
                String line = sc.nextLine();
                String[] words = line.split("\\s+");
                // print request header on console
                System.out.println( "Request header" );
                System.out.println( line );
                while ( sc.hasNextLine() ) {
                    line = sc.nextLine();
                    if ( line.length() == 0 ) break; // end of header
                    System.out.println( line );
                }
                if ( words[1].equals("/favicon.ico") ) { // possible bug?
                    sendNotFound( ps );
                }
                else {
                    sendReply( words[1], ps );
                }
                ps.close();
                sc.close();
                sock.close();
            }
       }
       catch( IOException e ) {
           System.out.println("error: " + e );
       }
    }
}
