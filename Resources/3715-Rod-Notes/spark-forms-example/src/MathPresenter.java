public abstract class MathPresenter {

    private static final String ERROR_PAGE =
        "<html>" +
        "<head><title>Math Calculations</title></head>" +
        "<body> <h1>Error</h1> %s </body>" +
        "</html>";

    private int x = 0, y = 0;
    
    public int getX() { return x; }
    public int getY() { return y; }

    // return null if parse OK, otherwise return error page
    public String parseParams( String x, String y ) {
        if ( x == null || y == null ) {
            return String.format( ERROR_PAGE, "missing params" );
        }
        try {
            this.x = Integer.parseInt( x );
            this.y = Integer.parseInt( y );
        }
        catch( NumberFormatException ex ) {
            return String.format( ERROR_PAGE, ex.getMessage() );
        }
        return null;
    }

    public abstract String getAddPage();
    public abstract String getMultPage();
}
