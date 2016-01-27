package chat;

import java.util.ArrayList;
import java.util.Iterator;
import java.io.Serializable;

public class Chat implements Serializable {
    private ArrayList<Phrase> phrases =
        new ArrayList<Phrase>();

    private class ChatIterator implements
        Iterator<Phrase>
    {
        private int index = 0;

        public ChatIterator( int startIndex ) {
            this.index = startIndex;
        }

        public synchronized boolean hasNext() {
            return index < phrases.size();
        }

        public synchronized Phrase next() {
            Phrase ph = phrases.get( index );
            index++;
            return ph;
        }

        public void remove() throws UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }
    }

    public Chat() {} 

    public synchronized void addPhrase( Phrase ph ) {
        phrases.add( ph );
        // notifyAll is required to alert threads
        // waiting for new phrases.
        notifyAll();
    }

    public Iterator<Phrase> getPhrasesAfter( int start ) {
        return new ChatIterator( start );
    }

    public synchronized Iterator<Phrase>
    getPhrasesAfterWait( int start, int waitTime ) {
        while( start >= phrases.size() ) {
            try {
                // the wait will block the servlet thread
                // until it times out, or new phrases arrive
                wait( waitTime );
                if ( start >= phrases.size() ) return null;
            }
            catch( InterruptedException ex ) {
                return null;
            }
        }
        return new ChatIterator( start );
    }

    public synchronized int size() {
        return phrases.size();
    }
}
