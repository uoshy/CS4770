import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

class CpBuff {
    public static void main( String[] args ) {
        try {
            FileInputStream in = new FileInputStream( args[0] );
            FileOutputStream out = new FileOutputStream( args[1] );

            byte b[] = new byte[1024];
            int amt = 0;
            while ( (amt=in.read( b )) >= 0 ) {
                out.write( b, 0, amt );
            }
            in.close();
            out.close();
        }
        catch( IOException e ) {
           System.out.println("error: " + e );
        }
    }
}
