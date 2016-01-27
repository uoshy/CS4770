import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.io.InputStream;
import java.io.DataInputStream;
import java.io.BufferedInputStream;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.BufferedOutputStream;

class CalculateServer {
    private ServerSocket listener;
    private CalculateLogger logger;

    public CalculateServer( int port ) throws IOException {
        /* determine the address of the server and connect to it */
        listener = new ServerSocket( port );
        logger = new CalculateLogger("calc.log");
    }

    public void serve() throws IOException {
        while ( true ) {
            Socket sock = listener.accept();
            logger.report("accept: " + sock );
            try {
                Calculator calc =
                    new Calculator(
                        sock.getInputStream(),
                        sock.getOutputStream() );
                calc.run();
            }
            catch( IOException  e ) {
                throw new RuntimeException( e );
            }
            sock.close();
            logger.report("close: " + sock );
        }
    }

    public static void main( String[] args ) {
        int port = 0;
        String host = null;
        try {
            port = Integer.parseInt( args[0] );
        }
        catch( NumberFormatException e ) {
            System.out.println("bad port number");
            System.exit(0);
        }
        catch( IndexOutOfBoundsException e ) {
            System.out.println("usage: java CalculateServer port");
            System.exit(0);
        }

        try {
            CalculateServer server = new CalculateServer( port );
            server.serve();
        }
        catch( IOException e ) {
            System.out.println("io error:" + e);
            System.exit(0);
        }
    }
}

class Calculator {
    private DataInputStream requests;
    private DataOutputStream replys;
    
    public Calculator( InputStream in, OutputStream out ) {
        requests = new DataInputStream( new BufferedInputStream( in ));
        replys = new DataOutputStream( new BufferedOutputStream( out ));
    }

    public void run() {
        try {
            while( true ) {
                int cmd = requests.readInt();
                if ( cmd == CalculatorCommands.EXIT ) {
                    break; 
                }
                else if ( cmd == CalculatorCommands.ADD ) {
                    replys.writeInt(
                        requests.readInt() + requests.readInt() );
                    replys.flush();
                }
                else if ( cmd == CalculatorCommands.MULT ) {
                    replys.writeInt(
                        requests.readInt() * requests.readInt() );
                    replys.flush();
                }
                else if ( cmd == CalculatorCommands.SUBTRACT ) {
                    replys.writeInt(
                        requests.readInt() - requests.readInt() );
                    replys.flush();
                }
                else if ( cmd == CalculatorCommands.DIVIDE ) {
                    replys.writeInt(
                        requests.readInt() / requests.readInt() );
                    replys.flush();
                }
                else {
                    // unkown command, give up
                    break;
                }
            }
            requests.close();
            replys.close();
        }
        catch( IOException e ) {
            // just give up right now
            throw new RuntimeException( e );
        }
    }
}
