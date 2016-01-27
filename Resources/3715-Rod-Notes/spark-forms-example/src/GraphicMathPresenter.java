public class GraphicMathPresenter extends MathPresenter {
    private static final int BLOCK_HEIGHT = 10;

    private static final String PAGE = 
        "<html>" +
        "<head>" +
        "<style type='text/css'> div.b{background:red; margin: 5px;}</style>" +
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

    @Override
    public String getAddPage() {
        String xv = block( getX(), BLOCK_HEIGHT) + " and <br>";
        String yv = block( getY(), BLOCK_HEIGHT) + " is <br>";
        String sum = block(getX()+getY(), BLOCK_HEIGHT);
        return String.format( PAGE, xv, yv, sum );
    }

    @Override
    public String getMultPage() {
        String xv = block( getX(), BLOCK_HEIGHT) + " and <br>";
        String yv = block( getY(), BLOCK_HEIGHT) + " is <br>";
        String prod = block(getX(), getY());
        return String.format( PAGE, xv, yv, prod );
    }
}
