/**
 * Create an Iterator using a nested class.
 */
public class IntVect1 extends BaseIntVect {

    public IntVect1( int sz ) {
        super( sz );
    }

    /**
     * Declare nested class.
     */
    public static class Iterator {
        private IntVect1 vect;
        private int index;

        /**
         * Iterates over the vect object.
         */
        public Iterator( IntVect1 vect ) {
            this.vect = vect;
            this.index = 0;
        }

        public boolean hasNext() {
            return index < vect.length ;
        }

        public int nextInt() {
            int v = vect.array[index];
            index++;
            return v;
        }
    }

    /**
     * Create and return an iterator object.
     */
    public Iterator iterator() {
        return new Iterator( this );
    }

    public static void main( String[] args ) {
        IntVect1 vect = new IntVect1(10);
        vect.add( 2 );
        vect.add( 4 );
        vect.add( 8 );
        IntVect1.Iterator it = vect.iterator();
        while( it.hasNext() ) {
            System.out.println( it.nextInt() );
        }
    }
}
