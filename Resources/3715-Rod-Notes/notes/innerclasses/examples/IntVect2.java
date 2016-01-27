public class IntVect2  extends BaseIntVect {

    public IntVect2( int sz ) {
        super( sz );
    }

    /**
     * Declare inner class.
     */
    public class Iterator {
        private int index;

        public Iterator() {
            index = 0;
        }

        public boolean hasNext() {
            return index < length ;
        }

        public int nextInt() {
            int v = array[index];
            index++;
            return v;
        }
    }

    public Iterator iterator() {
        // "this" is associated with the new iterator
        return new Iterator();
    }

    public static void main( String[] args ) {
        IntVect2 vect = new IntVect2(10);
        vect.add( 1 );
        vect.add( 4 );
        vect.add( 7 );
        IntVect2.Iterator it = vect.iterator();
        while( it.hasNext() ) {
            System.out.println( it.nextInt() );
        }
    }
}
