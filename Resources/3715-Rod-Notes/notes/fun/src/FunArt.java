import java.util.Timer;
import java.util.TimerTask;
import java.io.IOException;

public class FunArt {
    public static void main( String[] args ) throws IOException {
        final StepControl step = new StepControl();
        Timer timer = new Timer( true ); // mark as Daemon
        timer.schedule( new TimerTask() {
            public void run() {
                step.advanceStep();
            }
        }, 50, 100 );

        ScreenArt screen = new ScreenArt( System.out );
        screen.clear();
        HorizontalLine line1 = new HorizontalLine(3, 10, 40, '@', step, screen);
        Thread t1 = new Thread( line1 );
        HorizontalLine line2 = new HorizontalLine(16, 10, 50, '*',step,screen);
        Thread t2 = new Thread( line2 );
        HorizontalLine line3 = new HorizontalLine(10, 1, 80, '%', step, screen);
        Thread t3 = new Thread( line3 );
        HorizontalLine line4 = new HorizontalLine(20, 1, 80, '-', step, screen);
        Thread t4 = new Thread( line4 );

        t1.start();
        t2.start();
        //t3.start();
        //t4.start();
    }
}
