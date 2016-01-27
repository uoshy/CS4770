import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.io.IOException;

import java.net.UnknownHostException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.InetAddress;

import java.util.Scanner;

class CalculateClient {
    private InetAddress serverHost;
    private int serverPort;
    private Socket calculate;
    private DataInputStream reply;
    private DataOutputStream request;

    public CalculateClient( String host, int port ) throws IOException {
        /* determine the address of the server and connect to it */
        serverHost = InetAddress.getByName( host );
        serverPort = port;
        calculate = new Socket( serverHost, serverPort );

        /* get the output stream */
        OutputStream out = calculate.getOutputStream();
        request = new DataOutputStream( new BufferedOutputStream( out ));

        /* get the input stream */
        InputStream in = calculate.getInputStream();
        reply = new DataInputStream( new BufferedInputStream( in ));

    }

    public void handleUserInput() throws IOException {
        Scanner user = new Scanner( System.in ); 

        System.out.print("calc: ");
        while( user.hasNextLine() ) {
            String line = user.nextLine();
            Scanner parse = new Scanner( line );
            if ( !parse.hasNext() ) continue;
            String cmd = parse.next();
            if ( cmd.equals("add") ) {
                int a = parse.nextInt();
                int b = parse.nextInt();
                request.writeInt( CalculatorCommands.ADD );
                request.writeInt( a );
                request.writeInt( b );
                request.flush();
                System.out.println( "result = " + reply.readInt() );
            }
            else if ( cmd.equals("mult") ) {
                int a = parse.nextInt();
                int b = parse.nextInt();
                request.writeInt( CalculatorCommands.MULT );
                request.writeInt( a );
                request.writeInt( b );
                request.flush();
                System.out.println( "result = " + reply.readInt() );
            }
            else if ( cmd.equals("sub") ) {
                int a = parse.nextInt();
                int b = parse.nextInt();
                request.writeInt( CalculatorCommands.SUBTRACT );
                request.writeInt( a );
                request.writeInt( b );
                request.flush();
                System.out.println( "result = " + reply.readInt() );
            }
            else if ( cmd.equals("divide") ) {
                int a = parse.nextInt();
                int b = parse.nextInt();
                request.writeInt( CalculatorCommands.DIVIDE );
                request.writeInt( a );
                request.writeInt( b );
                request.flush();
                System.out.println( "result = " + reply.readInt() );
            }
            else if ( cmd.equals("exit") ) {
                request.writeInt( CalculatorCommands.EXIT );
                request.flush();
                break; // exit
            }
            System.out.print("calc: ");
        }
        request.close();
        reply.close();
    }


    public static void main( String[] args ) {
        int port = 0;
        String host = null;
        try {
            host = args[0];
            port = Integer.parseInt( args[1] );
        }
        catch( NumberFormatException e ) {
            System.out.println("bad port number");
            System.exit(0);
        }
        catch( IndexOutOfBoundsException e ) {
            System.out.println("usage: cmd host port");
            System.exit(0);
        }

        try {
            CalculateClient calc = new CalculateClient( host, port );
            calc.handleUserInput();
        }
        catch( UnknownHostException e ) {
            System.out.println("bad host name");
            System.exit(0);
        }
        catch( IOException e ) {
            System.out.println("io error:" + e);
            System.exit(0);
        }
    }
}
