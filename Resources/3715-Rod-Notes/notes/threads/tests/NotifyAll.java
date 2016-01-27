import java.util.Scanner;

class Block {
    private boolean block = true;

    public synchronized void unblock() {
        block = false;
        notifyAll();
    }

    public synchronized void doesBlock( String mesg ) {
        while( block ) {
            System.out.println( "wait:" + mesg + " " + block );
            try {
                wait();
            }
            catch( InterruptedException ex ) {
                System.out.println("interrupted: " + mesg );
            }
            System.out.println( "notified: " + mesg + " " + block );
        }
        block = true;
    }
}

class BlockingThread extends Thread {
     private Block block;
     private String mesg;

     public BlockingThread( String mesg, Block block ) {
         this.mesg = mesg;
         this.block = block;
     }

     public void run() {
         block.doesBlock( mesg );
     }
}

public class NotifyAll {
    public static void main( String[] args ) {
        int numThreads = Integer.parseInt( args[0] );
        Block block = new Block();
        BlockingThread[] threads = new BlockingThread[numThreads];

        for ( int i = 0 ; i < numThreads; i++ ) {
            String m = "th" + i;
            BlockingThread t = new BlockingThread(m, block);
            t.start();
            threads[ i ] = t;
        }

        Scanner sc = new Scanner( System.in );
        System.out.print("cmd: ");
        while ( sc.hasNextLine() ) {
            String line = sc.nextLine();
            if ( line.startsWith("exit" ) ) {
                System.exit( 1 );
            }
            else {
                block.unblock();
            }
            System.out.print("cmd: ");
        }
    }
}
