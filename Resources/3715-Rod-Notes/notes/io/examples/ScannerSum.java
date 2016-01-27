import java.io.IOException;
import java.io.File;
import java.util.Scanner;

/* New to jdk 1.5 */
public class ScannerSum {
    public static void main( String[] args ) throws IOException {
        Scanner scanner = new Scanner(new File(args[0]));
        int sum = 0;
        while( scanner.hasNextInt() ) {
            sum += scanner.nextInt();
        }
        System.out.println( sum );
    }
}
