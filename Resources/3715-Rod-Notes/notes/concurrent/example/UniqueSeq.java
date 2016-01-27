/**
 * Provides a unique sequence number that can be invoked from
 * different threads. Use the Singleton design pattern to
 * ensure only one UniqueSeq is created.
 */
public class UniqueSeq {
    private int sequence;

    // private ensures only getUniqueSeq can construct
    // this object
    private UniqueSeq() {
        sequence = 0;
    }

    // only one thread at a time
    public synchronized int nextSeq() {
        int s = sequence;
        sequence++;
        return s;
    }

    private static UniqueSeq singleton = null;

    public static UniqueSeq getUniqueSeq() {
        if ( singleton == null ) {
            singleton = new UniqueSeq();
        }
        return singleton;
    }
}
