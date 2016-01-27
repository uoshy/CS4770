/**
 * demonstrates a static method class.
 */
public class IntVect4 extends BaseIntVect {

    public IntVect4( int sz ) {
        super( sz );
    }

    public interface Iterator {
        public boolean hasNext();
        public int nextInt();
    }

    public static void main( String[] args ) {
        final IntVect4 vect = new IntVect4(10);
        vect.add( 7 );
        vect.add( 4 );
        vect.add( 6 );

        /*
         * Declare a static method class, can only
         * access final and static variables and methods.
         */
        IntVect4.Iterator it = new IntVect4.Iterator() {
            private int index = 0;
            public boolean hasNext() {
                return index < vect.size() ;
            }
            public int nextInt() {
                return vect.get( index++ );
            }
        };
        while( it.hasNext() ) {
            System.out.println( it.nextInt() );
        }
    }
}
