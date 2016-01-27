import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class DataOutTest {
    public static void main( String[] args ) throws IOException {
        DataOutputStream ds =
            new DataOutputStream( new FileOutputStream( "data" ));
        ds.writeChar( 'A' );
        ds.writeInt( 47 );
        ds.writeDouble( Math.PI );
        ds.writeUTF("hi there");
        ds.close();
    }
}
