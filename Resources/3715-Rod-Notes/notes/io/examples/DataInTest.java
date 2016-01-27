import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class DataInTest {
    public static void main( String[] args ) throws IOException {
        DataInputStream ds =
            new DataInputStream( new FileInputStream( "data" ));
        System.out.println( ds.readChar() );
        System.out.println( ds.readInt() );
        System.out.println( ds.readDouble() );
        System.out.println( ds.readUTF() );
        ds.close();
    }
}
