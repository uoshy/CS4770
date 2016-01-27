package chat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.UnsupportedEncodingException;

import java.util.Iterator;
import java.util.Date;

public class ChatServlet extends HttpServlet {
    private static final String ADD = "add";
    private static final String NEXT = "next";
    private static final String START = "start";
    private static final String PHRASE = "phrase";
    private static final String SPEAKER = "speaker";
    private static final int WAIT_TIME = 30000; // 30 seconds

    private Chat chat = new Chat();

    protected void doPost(
        HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        doGet( request, response);
    }

    private void sendReply(
        HttpServletResponse response, String top, String msg )
        throws ServletException, IOException
    {
        log( top + ":" + msg );
        response.setContentType("text/xml");
        PrintWriter out = response.getWriter();
        out.print("<" + top + ">" );
        out.print( msg );
        out.println("</" + top + ">" );
    }

    protected void doGet(
        HttpServletRequest request, HttpServletResponse response )
        throws ServletException, IOException
    {
        String info = request.getPathInfo();
        if ( info == null || info.length() < 1 ) {
            sendReply( response, "error", "bad url" );
            return;
        }
        info = info.substring(1);
        if ( info.equals( NEXT ) ) {
            processNext( request, response );
        }
        else if ( info.equals( ADD ) ) {
            // this blocks for a fixed amount of time
            processAdd( request, response );
        }
        else {
            sendReply( response, "error", "bad url" );
        }
    }

    private void processAdd(
        HttpServletRequest request, HttpServletResponse response )
        throws ServletException, IOException
    {
        response.setContentType("text/xml");
        PrintWriter out = response.getWriter();
        String phrase = request.getParameter( PHRASE );
        if ( phrase == null ) {
            log( "error: missing phrase" );
            out.println("<error>missing phrase</error>");
            return;
        }
        String speaker = request.getParameter( SPEAKER );
        if ( speaker == null ) {
            log( "error: missing speaker" );
            out.println("<error>missing speaker</error>");
            return;
        }
        String host = request.getRemoteAddr();
        Phrase ph = new Phrase(speaker, phrase, host );
        chat.addPhrase( ph );
        out.println("<done>added</done>");
        log( chat.size() + ":" + speaker + ":" + phrase );
    }

    private void processNext(
        HttpServletRequest request, HttpServletResponse response )
        throws ServletException, IOException
    {
        String st = request.getParameter( START );
        if ( st == null ) {
            sendReply( response, "error", "missing start" );
            return;
        }
        int start = -1;
        try {
            start = Integer.parseInt( st );
        }
        catch( NumberFormatException ex ) {
            sendReply( response, "error", "illegal start" );
            return;
        }
        Iterator<Phrase> it =
            chat.getPhrasesAfterWait(start, WAIT_TIME);
        if ( it == null ) {
            sendReply( response, "timeout", "timed out" );
            return;
        }
        response.setContentType("text/xml");
        PrintWriter out = response.getWriter();
        outputPhrases( out, it);
    }

    private static void outputPhrases(
        PrintWriter out, Iterator<Phrase> it )
    {
        out.println( "<div xmlns='http://www.w3.org/1999/xhtml'>" );
        while ( it.hasNext() ) {
            Phrase ph = it.next();
            out.println("<div class='phrase'>");
            Date d = ph.getDate();
            out.println("<div class='date'>" + d + "</div>");
            String h = ph.getHost();
            if ( h != null ) {
                 out.println("<div class='host'>" + h + "</div>");
            }
            String s = ph.getSpeaker();
            if ( s != null ) {
                 out.println("<div class='speaker'>" + s + "</div>");
            }
            String m = ph.getPhrase();
            if ( m != null ) {
                 out.println("<div class='phrase'>" + m + "</div>");
            }
            out.println("</div>");
        }
        out.println( "</div>" );
    }
}
