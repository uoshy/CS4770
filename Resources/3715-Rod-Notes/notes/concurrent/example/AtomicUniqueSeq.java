/**
 * Provides a unique sequence number that can be invoked safely
 * from different threads. Use the Singleton design pattern to
 * ensure only one UniqueSeq is created.
 */
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicUniqueSeq {
    private AtomicInteger sequence;

    // private ensures only getUniqueSeq can construct
    // this object
    private AtomicUniqueSeq() {
        sequence = new AtomicInteger(0);
    }

    /**
     * Return the next unique seqence number.
     */
    public int nextSeq() {
        return sequence.getAndIncrement();
    }

    private static AtomicUniqueSeq singleton = null;

    public static AtomicUniqueSeq getUniqueSeq() {
        if ( singleton == null ) {
            singleton = new AtomicUniqueSeq();
        }
        return singleton;
    }
}
