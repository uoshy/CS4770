import java.io.IOException;
import java.io.InputStreamReader;

public class UpperConvert {
    public static void main( String[] args ) throws IOException {
        InputStreamReader rd = new InputStreamReader(System.in);
        int ch = 0;

        while( (ch=rd.read()) != -1 ) {
            char c = (char)ch;
            System.out.print( Character.toUpperCase( c ) );
        }
    }
}
