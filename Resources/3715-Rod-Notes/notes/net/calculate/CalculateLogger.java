import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CalculateLogger {
    private BufferedWriter logfile;
    private String filename;

    public CalculateLogger( String filename)
        throws IOException
    {
        this.filename = filename;
        this.logfile =
            new BufferedWriter( new FileWriter( filename ));
    }

    public void report( String s ) {
        try {
            logfile.write( s + "\n" );
            logfile.flush();
        }
        catch( IOException ex ) {
            // ignore logging errors
        }
    }

    public void close() {
        try {
            logfile.close();
        }
        catch( IOException ex ) {
            // ignore logging errors
        }
    }
}
