import java.io.IOException;
import java.io.DataInputStream;

class DataIn {
    public static void main( String[] args ) throws IOException {
        DataInputStream in = new DataInputStream( System.in );

        System.out.println("int = " + in.readInt() );
        System.out.println("double = " + in.readDouble() );
        System.out.println("chars = " + in.readUTF() );
        in.close();
    }
}
