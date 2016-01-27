import java.io.PrintWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiLineServerTP implements Runnable {
    private Socket sock;

    public MultiLineServerTP( Socket sock ) {
        this.sock = sock;
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
        // create 5 thread pool threads
        ExecutorService tpes = Executors.newFixedThreadPool(5);

        try {
            ServerSocket listen = new ServerSocket( 0 );
            System.out.println("Server port is " + listen.getLocalPort() );

            while ( true ) {
                Socket sock = listen.accept();
                tpes.execute( new MultiLineServer( sock ));
                dumpThreads();
            }
        }
        catch( IOException e ) {
            System.out.println("error: " + e );
        }
        tpes.shutdown();
    }
}
