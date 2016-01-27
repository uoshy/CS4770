import java.io.InputStream;
import java.io.FilterInputStream;
import java.io.IOException;

import java.io.ByteArrayInputStream;

public class CountInputStream extends FilterInputStream {

    private byte countByte;
    private int count;

    public CountInputStream( InputStream in, byte cb ) {
        super( in );
        countByte = cb;
        count = 0;
    }

    public int read() throws IOException {
        int b = super.read();
        if ( (byte)b == countByte ) {
            count++;
        }
        return b;
    }

    public int getCount() {
        return count;
    }

    public static void main( String[] args ) throws IOException {
        byte[] data = { 1, 2, 3, 1, 7, 9, 8};
        ByteArrayInputStream bs = new ByteArrayInputStream( data );
        CountInputStream cs = new CountInputStream(bs, (byte)1);
        
        int b;
        while( (b=cs.read()) != -1 ) {
            System.out.println("b = " + b );
        }
        System.out.println("Count of 1's: " + cs.getCount() );
    }
}
