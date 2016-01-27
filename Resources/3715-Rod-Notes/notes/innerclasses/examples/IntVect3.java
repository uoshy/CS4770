public class IntVect3 extends BaseIntVect {

    public IntVect3( int sz ) {
        super( sz );
    }

    /* 
     * Interfaces can also be declared inside of a class.
     */
    public interface Iterator {
        public boolean hasNext();
        public int nextInt();
    }

    /**
     * Declare and return method class. These are also called
     * anonymous classes.
     */
    public Iterator iterator() {
        //                + The interface being implemented
        //                v
        Iterator it = new Iterator() {
            private int index = 0;

            public boolean hasNext() {
                return index < length;
            }

            public int nextInt() {
                int v = array[index];
                index++;
                return v;
            }
        }; // end of assignment statement
        return it;
    }

    public static void main( String[] args ) {
        IntVect3 vect = new IntVect3(10);
        vect.add( 3 );
        vect.add( 2 );
        vect.add( 1 );
        IntVect3.Iterator it = vect.iterator();
        while( it.hasNext() ) {
            System.out.println( it.nextInt() );
        }
    }
}
