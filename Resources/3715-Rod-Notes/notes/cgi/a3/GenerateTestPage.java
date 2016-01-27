import java.util.Random;

public class GenerateTestPage {

    private static long parseStudentId( String query )
	throws NumberFormatException, IllegalArgumentException
    {
        String [] params = query.split("&");
	for( int i = 0 ; i < params.length; i++ ) {
	    String [] words = params[i].split("=");
	    if ( words.length != 2 ) continue;
	    if ( words[0].equals("student") ) {
	        return Long.parseLong( words[1] );
	    }
	}
	throw new IllegalArgumentException("missing student param");
    }

    private static final int MAX_LIST = 10;
    private static final int MAX_SEED = 1000;
    private static String genseq = "genseq.sh";

    private static int[] generateList() {
        int[] seeds = new int[MAX_LIST];
	Random rd = new Random();
	System.out.println("<ol>");
	for( int i = 0 ; i < seeds.length; i++ ) {
	    seeds[i] = rd.nextInt( MAX_SEED );
	    String url = genseq + "?seed=" + seeds[i];
	    System.out.println("<li>");
	    System.out.print("<a href='" + url + "'>" ); 
	    System.out.println( seeds[i] + "</a>" ); 
	    System.out.println("</li>");
	}
	System.out.println("</ol>");
	return seeds;
    }

    public static void main( String[] args ) {
	long student;
	TransactionLog log;
	try {
	    student = parseStudentId( System.getenv("QUERY_STRING") );
	    log = new TransactionLog( "transaction.log" );
	    log.record("test: " + student );
	}
	catch( Exception ex ) {
	    System.out.print("Content-Type: text/plain\r\n\r\n");
	    System.out.println(ex.getMessage() );
	    return;
	}
	System.out.print("Content-Type: text/html\r\n\r\n");
	System.out.println("<html><head></head><body>" );
	int[] seeds;
	try {
	    seeds = generateList();
	    StringBuffer sb = new StringBuffer();
	    sb.append( "sum: " + student);
	    for( int i = 0 ; i < seeds.length; i++ ) {
	        sb.append( " " + seeds[i] );
	    }
	    log.record( sb.toString() );
	    log.close();
	}
	catch( Exception ex ) {
	    // ignore error, XXX
	}
	finally {
	    System.out.println("</body></html>" );
	}
    }
}
