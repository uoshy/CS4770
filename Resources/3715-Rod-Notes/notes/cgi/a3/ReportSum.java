public class ReportSum {

    private static long[] parseParams( String query )
	throws NumberFormatException, IllegalArgumentException
    {
        long[] result = new long[2];

        String [] params = query.split("&");
	for( int i = 0 ; i < params.length; i++ ) {
	    String [] words = params[i].split("=");
	    if ( words.length != 2 ) continue;
	    if ( words[0].equals("student") ) {
	        result[0] = Long.parseLong( words[1] );
	    }
	    else if ( words[0].equals("sum") ) {
	        result[1] = Long.parseLong( words[1] );
	    }
	    else {
		throw new IllegalArgumentException("unknown param");
	    }
	}
	return result;
    }

    public static void main( String[] args ) {
	long[] params;
	TransactionLog log;
	try {
	    params = parseParams( System.getenv("QUERY_STRING") );
	    log = new TransactionLog( "transaction.log" );
	    String remote = System.getenv("REMOTE_ADDR");
	    if ( remote == null ) remote = "";
	    log.record("result: " + params[0] + " " + params[1] + " " + remote);
	}
	catch( Exception ex ) {
	    System.out.print("Content-Type: text/plain\r\n\r\n");
	    System.out.println(ex.getMessage() );
	    return;
	}
	System.out.print("Content-Type: text/plain\r\n\r\n");
	System.out.print("The " + params[1] + " for " + params[0] );
	System.out.println(" has been recorded" );
    }
}
