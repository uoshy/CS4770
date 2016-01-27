import java.io.PrintWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

class MultiLineServer extends Thread {
    private Socket sock;

    public MultiLineServer( Socket sock ) {
        this.sock = sock;
        start();
    }

    public void run() {
        try {
            /* data from client */
            Scanner rd = new Scanner(  sock.getInputStream() );

            /* data to client */
            PrintWriter pw = new PrintWriter(sock.getOutputStream(), true);

            while ( rd.hasNextLine() ) {
                String line = rd.nextLine();
                line = line.toUpperCase();
                pw.println( line );
                System.out.println( line );
            }
            System.out.println( "closing" + sock );
            sock.close(); // clean up required
            rd.close();
            pw.close();
        }
        catch( IOException e ) {
            System.out.println("error: " + e );
        }
    }

    private static void dumpThreads() {
        int count = Thread.activeCount();
        Thread[] threads = new Thread[count];
        int num = Thread.enumerate( threads );
        for( int i = 0 ; i < num; i++ ) {
            System.out.println( threads[i] );
        }
    }

    public static void main( String[] args ) {
        try {
            ServerSocket listen = new ServerSocket( 0 );
            System.out.println("Server port is " + listen.getLocalPort() );

            while ( true ) {
                Socket sock = listen.accept();
                new MultiLineServer( sock );
                dumpThreads();
            }
        }
        catch( IOException e ) {
            System.out.println("error: " + e );
        }
    }
}
