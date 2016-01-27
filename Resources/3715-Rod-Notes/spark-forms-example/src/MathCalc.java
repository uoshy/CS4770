public class MathCalc {
    private static final int BLOCK_HEIGHT = 10;

    private static final String ERROR_PAGE = 
        "<html>" +
        "<head><title>Math Calculations</title></head>" +
        "<body> <h1>Error</h1> %s </body>" +
        "</html>";

    private static final String PAGE = 
        "<html>" +
        "<head>" +
        "<style type='text/css'> div.b {background:red; margin: 5px;}</style>" +
        "<title>Math Calculations</title>" +
        "</head>" +
        "<body> <h1>The Result</h1>" +
        "%s <br> %s <br> %s </body>" +
        "</html>";

    private static String block( int x, int y ) {
        return String.format(
            "<div class='b' style='width:%d ; height:%d'></div>",
            x, y );
    }

    public static String addPage( String x, String y ) {
        if ( x == null || y == null ) {
            return String.format( ERROR_PAGE, "missing params" );
        }
        int ix, iy;
        try {
            ix = Integer.parseInt( x );
            iy = Integer.parseInt( y );
        }
        catch( NumberFormatException ex ) {
            return String.format( ERROR_PAGE, ex.getMessage() );
        }
        String xv = block(ix, BLOCK_HEIGHT) + " and <br>";
        String yv = block(iy, BLOCK_HEIGHT) + " is <br>";
        String sum = block(ix+iy, BLOCK_HEIGHT);
        return String.format( PAGE, xv, yv, sum );
    }

    public static String multPage(String x, String y) {
        if ( x == null || y == null ) {
            return String.format( ERROR_PAGE, "missing params" );
        }
        int ix, iy;
        try {
            ix = Integer.parseInt( x );
            iy = Integer.parseInt( y );
        }
        catch( NumberFormatException ex ) {
            return String.format( ERROR_PAGE, ex.getMessage() );
        }
        String xv = block(ix, BLOCK_HEIGHT) + " and <br>";
        String yv = block(iy, BLOCK_HEIGHT) + " is <br>";
        String prod = block(ix, iy);
        return String.format( PAGE, xv, yv, prod );
    }
}
