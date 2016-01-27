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

class CalculateClientRefactored {
    private InetAddress serverHost;
    private int serverPort;
    private Socket calculate;
    private DataInputStream reply;
    private DataOutputStream request;

    public CalculateClientRefactored(
            String host, int port )
    throws IOException
    {
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

    private int doAdd( int a, int b ) throws IOException {
        request.writeInt( CalculatorCommands.ADD );
        request.writeInt( a );
        request.writeInt( b );
        request.flush();
        return reply.readInt();
    }

    private int doSub( int a, int b ) throws IOException {
        request.writeInt( CalculatorCommands.SUBTRACT );
        request.writeInt( a );
        request.writeInt( b );
        request.flush();
        return reply.readInt();
    }

    private int doMult( int a, int b ) throws IOException {
        request.writeInt( CalculatorCommands.MULT );
        request.writeInt( a );
        request.writeInt( b );
        request.flush();
        return reply.readInt();
    }

    private int doDivide( int a, int b ) throws IOException {
        request.writeInt( CalculatorCommands.DIVIDE );
        request.writeInt( a );
        request.writeInt( b );
        request.flush();
        return reply.readInt();
    }

    private void doExit() throws IOException {
        request.writeInt( CalculatorCommands.EXIT );
        request.flush();
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
                System.out.println( "result = " + doAdd(a,b) );
            }
            else if ( cmd.equals("mult") ) {
                int a = parse.nextInt();
                int b = parse.nextInt();
                System.out.println( "result = " + doMult(a,b) );
            }
            else if ( cmd.equals("sub") ) {
                int a = parse.nextInt();
                int b = parse.nextInt();
                System.out.println( "result = " + doSub(a,b) );
            }
            else if ( cmd.equals("divide") ) {
                int a = parse.nextInt();
                int b = parse.nextInt();
                System.out.println( "result = " + doDivide(a,b) );
            }
            else if ( cmd.equals("exit") ) {
                doExit();
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
