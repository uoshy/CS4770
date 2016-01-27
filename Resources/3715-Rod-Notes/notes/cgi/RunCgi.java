import java.util.Map;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.File;
import java.io.FileOutputStream;

public class RunCgi {

    public static void main( String[] args ) throws Exception {
        ProcessBuilder pb = new ProcessBuilder("/bin/sh", "gentest.sh" );
        pb.directory( new File(".") );
        Map<String, String> env = pb.environment();
        env.clear();
        env.put( "QUERY_STRING", "student=123" );
        Process p = pb.start();
        InputStream instream = p.getInputStream();
        FileOutputStream save = new FileOutputStream("result.txt");
        int ch;
        while( (ch=instream.read()) != -1 ) {
            save.write( (byte)ch );
        }
        save.close();
    }
}
