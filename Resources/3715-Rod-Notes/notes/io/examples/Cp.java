import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Copy a file, byte by byte
 */
class Cp {
    public static void main( String[] args ) throws Exception {
        FileInputStream in = new FileInputStream( args[0] );
	FileOutputStream out = new FileOutputStream( args[1] );

	int b;
	while ( (b=in.read()) != -1 ) {
	    out.write( b );
	}
	in.close();
	out.close();
    }
}
