/*
 * Create a java.util.Iterator.
 */
public class IntVect5 extends BaseIntVect {

    public IntVect5( int sz ) {
        super( sz );
}
    /**
     * Declare and return method class. 
     */
    public java.util.Iterator iterator() {
        return new java.util.Iterator() {
            private int index = 0;

            public boolean hasNext() {
                return index < length;
            }

            public Object next() {
                int v = array[index];
                index++;
                return new Integer( v );
            }

            public void remove() {
                for( int i = index; i < length; i++ ) {
                    array[i-1] = array[i];
                }
                length--;
            }
        };
    }

    public static void main( String[] args ) {
        IntVect5 vect = new IntVect5(10);
        vect.add( 3 );
        vect.add( 2 );
        vect.add( 1 );
        java.util.Iterator it = vect.iterator();
        while( it.hasNext() ) {
            System.out.println( it.next() );
        }
    }
}
