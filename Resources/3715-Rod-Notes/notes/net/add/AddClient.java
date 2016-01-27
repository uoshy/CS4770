import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.UnknownHostException;
import java.net.InetAddress;
import java.net.Socket;

class AddClient {
    public static void main( String[] args ) {
        int range = 0;
        int port = 0;
        String host = null;
        try {
            host = args[0];
            port = Integer.parseInt( args[1] );
            range = Integer.parseInt( args[2] );
        }
        catch( NumberFormatException e ) {
            System.out.println("bad port number or range");
            System.exit(0);
        }
        catch( IndexOutOfBoundsException e ) {
            System.out.println("usage: java AddClient host port range");
            System.exit(0);
        }

        try {
            /* determine the address of the server and connect to it */
            InetAddress server = InetAddress.getByName( host );
            Socket sock = new Socket( server, port );
            DataInputStream din =
                new DataInputStream( sock.getInputStream() );
            DataOutputStream dout =
                new DataOutputStream( sock.getOutputStream() );

            /* tx size */
            dout.writeInt( range );
            /* tx ints to add */
            for( int i = 0 ; i < range; i++ ) {
                dout.writeInt( i );
            }
            dout.flush();
            /* retrieve result */
            int result = din.readInt();
            System.out.println("result is " + result );
            /* tell the server that we are done */
            dout.writeInt( -1 );
            dout.flush();
            sock.close();
            dout.close();
            din.close();
        }
        catch( UnknownHostException e ) {
            System.out.println("bad host name");
            System.exit(0);
        }
        catch( IOException e ) {
            System.out.println("io error:" + e);
            System.exit(0);
        }
    }
}
