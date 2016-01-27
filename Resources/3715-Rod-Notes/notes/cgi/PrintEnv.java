import java.util.Map;
import java.util.Set;

public class PrintEnv {
    public static void main( String[] args ) {
        Map<String,String> env = System.getenv();
        // Since the keys in the map must be unique,
        // they can be represented by a set
        Set<String> set = env.keySet();

        // iterate of the set and example the value
        for ( String n : set ) {
            System.out.println( n + "=" + env.get(n) );
        }
    }
}
