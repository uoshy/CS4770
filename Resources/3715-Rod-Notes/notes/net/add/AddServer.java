import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

class AddServer {
    public static void main( String[] args ) {
        try {
            ServerSocket listen = new ServerSocket( 0 );
            System.out.println("Server port is " + listen.getLocalPort() );

            /* handle one client at a time */
            while ( true ) {
                Socket sock = listen.accept();

                /* data from client */
                DataInputStream din =
                    new DataInputStream( sock.getInputStream() );

                /* data to client */
                DataOutputStream dout =
                    new DataOutputStream( sock.getOutputStream() );

                for(;;) {
                    int sum = 0;
                    int sz = din.readInt();
                    if ( sz <= 0 ) break;
                    for( int i = 0 ; i < sz; i++ ) {
                        sum += din.readInt();
                    }
                    dout.writeInt( sum );
                    dout.flush();
                }
                System.out.println("Connection closed");
                sock.close(); // clean up required
                din.close();
                dout.close();
            }
        }
        catch( IOException e ) {
            System.out.println("error: " + e );
        }
    }
}
