public class TextMathPresenter extends MathPresenter {

    private static final String PAGE = 
        "<html>" +
        "<head>" +
        "<title>Math Calculations</title>" +
        "</head>" +
        "<body> <h1>The Result</h1>" +
        "%s %s %s </body>" +
        "</html>";

    @Override
    public String getAddPage() {
        String xv = getX() + " plus ";
        String yv = getY() + " equals ";
        String sum = String.valueOf(getX() + getY());
        return String.format( PAGE, xv, yv, sum );
    }

    @Override
    public String getMultPage() {
        String xv = getX() + " times ";
        String yv = getY() + " equals ";
        String sum = String.valueOf(getX() * getY());
        return String.format( PAGE, xv, yv, sum );
    }
}
