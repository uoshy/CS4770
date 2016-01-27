/**
 * Create an Iterator using a nested class.
 */
/**
 * Provides an integer vector base class.
 * The is also an example of using inheritance
 * for reuse.
 */
public class BaseIntVect {
    protected int[] array;
    protected int length;

    public BaseIntVect( int sz ) {
        array = new int[sz];
        length = 0;
    }

    public void add( int v ) {
        if ( length >= array.length ) {
            int[] newarray = new int[array.length*2];
            System.arraycopy( array, 0, newarray, 0, array.length );
            array = newarray;
        }
        array[length] = v;
        length++;
    }

    public int get( int i ) {
        return array[i];
    }

    public int size() {
        return length;
    }

    public static void main( String[] args ) {
        IntVect1 vect = new IntVect1(10);
        vect.add( 2 );
        vect.add( 4 );
        vect.add( 8 );
        for( int i = 0 ; i < vect.size(); i++ ) {
            System.out.println( vect.get(i) );
        }
    }
}
