import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.File;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class TransactionLog {

    private FileOutputStream fout;
    private FileChannel channel;

    public TransactionLog( String filename ) throws IOException {
	File file = new File( filename );
	// open log file for append only
	fout = new FileOutputStream(file, true);
	channel = fout.getChannel();
    }

    public void record( String transaction ) throws IOException {
	// lock file when saving transaction
	FileLock lock = channel.lock();
	try {
	    PrintWriter pw = 
		new PrintWriter(new OutputStreamWriter(fout) );
	    pw.println( transaction );
	    pw.flush();
	}
	finally {
	    lock.release();
	}
    }

    public void close() throws IOException {
	fout.close();
    }
}
