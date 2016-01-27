import java.io.IOException;

public class HorizontalLine implements Runnable {
    private int row;
    private int startCol, endCol;
    private char movingChar;
    private StepControl step;
    private ScreenArt screen;

    private int stepNo;

    public HorizontalLine( int r, int c1, int c2, char ch,
        StepControl s, ScreenArt sc )
    {
        row = r;
        startCol = c1;
        endCol = c2;
        movingChar = ch;
        step = s;
        screen = sc;
        stepNo = 0;
    }

    public void run() {
        try {
            while ( true ) {
                for( int c = startCol; c <= endCol; c++ ) {
                    screen.moveCursor( row, c );
                    screen.byteOut( movingChar );

                    step.waitForStep( stepNo );

                    screen.moveCursor( row, c );
                    screen.byteOut( ' ' ); // erase character
                    stepNo++;
                }
            }
        }
        catch( IOException ex ) {
        }
        catch( InterruptedException ex ) {
        }
    }
}
