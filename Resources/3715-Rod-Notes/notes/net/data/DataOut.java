import java.io.IOException;
import java.io.DataOutputStream;

class DataOut {
    public static void main( String[] args ) throws IOException {
        DataOutputStream out = new DataOutputStream( System.out );

        out.writeInt( 47 );
        out.writeDouble( 3.1415926 );
        out.writeUTF( "Hello world" );
        out.flush();
        out.close();
    }
}
