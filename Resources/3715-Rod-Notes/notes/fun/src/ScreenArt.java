import java.io.OutputStream;
import java.io.IOException;

public class ScreenArt {
    private static final byte ESC = 0x1b; // ESC
    private static final byte SQB = 0x5b; // [
    private static final byte ZERO = 0x30; // 0
    private static final byte TWO = 0x32; // 2
    private static final byte FOUR = 0x34; // 4
    private static final byte UJ = (byte)'J'; // J
    private static final byte UH = (byte)'H'; // H
    private static final byte LH = (byte)'h'; // h
    private static final byte LL = (byte)'l'; // l
    private static final byte SEMICOLON = (byte)';'; // ;

    private static final byte[] clear_msg = { ESC, SQB, TWO, UJ };
    private static final byte[] replace_mode_msg = { ESC, SQB, FOUR, LL };
    private static final byte[] insert_mode_msg = { ESC, SQB, FOUR, LH };

    private OutputStream out;

    public ScreenArt( OutputStream out ) {
        this.out = out;
    }

    public void clear() throws IOException {
        synchronized ( out ) {
            out.write( clear_msg );
            out.flush();
        }
    }

    public void replaceMode() throws IOException {
        synchronized ( out ) {
            out.write( replace_mode_msg );
            out.flush();
        }
    }

    public void insertMode() throws IOException {
        synchronized ( out ) {
            out.write( insert_mode_msg );
            out.flush();
        }
    }

    private static byte toDigit( int d ) {
        return (byte)(ZERO + d);
    }

    public void moveCursor( int r, int c ) throws IOException {
        synchronized ( out ) {
            out.write( ESC );
            out.write( SQB );
            if ( r/10 != 0 ) out.write( toDigit( r / 10 ) );
            out.write( toDigit( r % 10 ) );
            out.write( SEMICOLON );
            if ( c/10 != 0 ) out.write( toDigit( c / 10 ) );
            out.write( toDigit( c % 10 ) );
            out.write( UH );
            out.flush();
        }
    }

    public void byteOut( int b ) throws IOException {
        out.write( b & 0xff );
        out.flush();
    }

    public static void main( String[] args ) throws Exception {
        ScreenArt art = new ScreenArt( System.out );
        art.clear();
        for ( int c = 10; c <= 50; c++ ) {
            art.moveCursor(4, c);
            art.byteOut( 'X' );
            Thread.sleep( 100 );
            art.moveCursor(4, c);
            art.byteOut( ' ' );
        }
        art.moveCursor(20, 1);
    }
}
