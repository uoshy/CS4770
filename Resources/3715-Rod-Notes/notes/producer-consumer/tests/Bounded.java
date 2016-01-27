public class Bounded implements Link {
    private Object[] buffer;
    private int head, tail, len;
    private boolean closed;

    public Bounded( int size ) {
        buffer = new Object[size];
        head = tail = 0;
        len = 0;
        closed = false;
    }

    public synchronized void send( Object o ) {
        while ( len >= buffer.length ) {
            try {
                wait();
            }
            catch( InterruptedException ex ) {
            }
        }
        buffer[ head ] = o;
        head = ( head + 1 ) % buffer.length;
        len++;
        notify();
    }

    public synchronized Object recv() {
        if ( len == 0 && closed )
            return null;
        while ( len == 0 ) {
            try {
                wait();
            }
            catch( InterruptedException ex ) {
            }
            if ( len == 0 && closed ) return null;
        }
        Object o = buffer[tail];
        tail = (tail+1) % buffer.length;
        len--;
        notify();
        return o;
    }

    public synchronized void close() {
        closed = true;
        notify();
    }

    public synchronized int getSize() {
        return len;
    }
}
