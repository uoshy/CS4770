import java.util.function.Consumer;
import java.util.ArrayList;
import java.util.Arrays;

public class PrintSkipping {
    public static void main( String[] args ) {
        // must be final or effectively final
        final String skip = "c";
        String[] arr = {"a", "b", "c", "d", "e"};

        ArrayList<String> al = new ArrayList<String>(Arrays.asList(arr));

        // anonymous inner class
        Consumer<String> c1 = new Consumer<String>() {
            public void accept( String t ) {
                if ( ! t.equals( skip ) ) {
                    System.out.println( t );
                }
            }
        };

        // print all except the one to skip
        System.out.println("Anonymous inner class");
        al.forEach( c1 );

        // lambda expression
        Consumer<String> c2 = (t) -> {
            if ( ! t.equals( skip ) ) {
                System.out.println( t );
            }
        };

        // print all except the one to skip
        System.out.println("lambda expression");
        al.forEach( c2 );
    }
}
