import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;

public class DatagramServ {
    public static void main( String[] args ) throws IOException{
        DatagramSocket ds;
        DatagramPacket dp;
        InetAddress ia;
        byte[] buf = new byte[1024];

        ds = new DatagramSocket();
        System.out.println("port = " + ds.getLocalPort() );

        /* set timeout to 5000 milliseconds (5 seconds) */
        ds.setSoTimeout( 5000 );

        while ( true ) {
            dp = new DatagramPacket( buf, buf.length );
            try {
                ds.receive( dp );
                int len = dp.getLength();
                System.out.println("rx packet: len=" + len );
                ia = dp.getAddress();
                System.out.println(
                    "from=(" + ia.getHostName() + ","
                    + dp.getPort() + ") text=" + new String(buf,0, len ) );
            }
            catch ( InterruptedIOException e ) {
                System.out.println("timed out");
            }    
        }
    }
}
