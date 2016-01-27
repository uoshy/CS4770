import java.io.PrintWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

class LineServer {
    public static void main( String[] args ) {
        try {
            ServerSocket listen = new ServerSocket( 0 );
            System.out.println("Server port is " + listen.getLocalPort() );

            /* handle one client at a time */
            while ( true ) {
                Socket sock = listen.accept();

                /* data from client */
                Scanner rd = new Scanner(sock.getInputStream());

                /* data to client */
                PrintWriter pw = new PrintWriter(sock.getOutputStream(), true);

                System.out.println("Accepted connection from "
                     + sock.getInetAddress() + " at port " 
                     + sock.getPort() );

                while ( rd.hasNextLine() ) {
                    String line = rd.nextLine();
                    line = line.toUpperCase();
                    pw.println( line );
                    System.out.println( line );
                }
                System.out.println( "closing" + sock );
                sock.close(); // clean up required
            }
        }
        catch( IOException e ) {
            System.out.println("error: " + e );
        }
    }
}
