import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Scanner;

public class DatagramClnt {
    public static void main( String[] args ) throws IOException{
	Scanner stdin = new Scanner( System.in );
        DatagramSocket ds; DatagramPacket dp;
        InetAddress ia; int port; byte[] buf;

        ia = InetAddress.getByName( args[0] );
        port = Integer.parseInt( args[1] );
        ds = new DatagramSocket();

        while ( stdin.hasNextLine() ) {
	    String line = stdin.nextLine();
            buf = line.getBytes();
            dp =  new DatagramPacket( buf, buf.length, ia, port );
            ds.send( dp );
        }
    }
}
