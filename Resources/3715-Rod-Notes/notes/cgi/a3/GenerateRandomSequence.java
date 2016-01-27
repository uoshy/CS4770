import java.util.Random;

public class GenerateRandomSequence {

    private static int parseSeed( String query )
	throws NumberFormatException, IllegalArgumentException
    {
        String [] params = query.split("&");
	for( int i = 0 ; i < params.length; i++ ) {
	    String [] words = params[i].split("=");
	    if ( words.length != 2 ) continue;
	    if ( words[0].equals("seed") ) {
	        return Integer.parseInt( words[1] );
	    }
	}
	throw new IllegalArgumentException("missing seed param");
    }

    private static final int MAX_SEQUENCE = 20;
    private static final int MAX_NUMBER = 100;

    public static void main( String[] args ) {
	 System.out.print("Content-Type: text/plain\r\n\r\n");
	 int seed;
	 try {
	     seed = parseSeed( System.getenv("QUERY_STRING") );
	 }
	 catch( Exception ex ) {
	     System.out.println(ex.getMessage() );
	     return;
	 }
	 Random seq = new Random( seed );
	 for( int i = 0 ; i < MAX_SEQUENCE; i++ ) {
	     System.out.println( seq.nextInt( MAX_NUMBER ) );
	 }
    }
}
