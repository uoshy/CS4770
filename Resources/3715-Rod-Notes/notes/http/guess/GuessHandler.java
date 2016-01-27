import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import java.net.URI;

import java.util.Random;
import java.util.Date;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class GuessHandler implements HttpHandler {
    private Random random = new Random();
    private int currentGuess = 0;

    public void handle(HttpExchange exchange) throws IOException {
        // setup the reply
        Headers responseHeaders = exchange.getResponseHeaders();
        responseHeaders.set("Content-Type", "text/html");
        exchange.sendResponseHeaders(200, 0);
        PrintWriter responseBody = new PrintWriter( exchange.getResponseBody());
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();

        // log request
        System.out.println("Handling: " + (new Date()) + ":" + uri );

        if ( path.equals("/guess") ) {
            currentGuess = random.nextInt(32) + 1;
            responseBody.println(
                "<html><head><title>Guess Number</title><body>" +
                "<p>Guess a number between 1 and 32</p>" +
                "<form action='/guess/theguess'>" +
                "<input type='text' name='ans'>" +
                "</form></body></html>"
            );
        }
        else if ( path.contains("/guess/theguess") ) {
            String query = uri.getQuery();
            if ( query.contains("ans=") ) {
                String[] words = query.split("=");
                try {
                    int i = Integer.parseInt( words[1] );
                    if ( i == currentGuess ) {
                        responseBody.println(
                            "<html><head><title>Guess Response</title><body>" +
                            "<p>Correct</p>" +
                            "<p><a href='/guess'>next guess</a></p>" +
                            "</body></html>"
                        );
                    }
                    else if ( i > currentGuess ) {
                        responseBody.println(
                            "<html><head><title>Guess Number</title><body>" +
                            "<p>Too high! Guess again</p>" +
                            "<form action='/guess/theguess'>" +
                            "<input type='text' name='ans'>" +
                            "</form></body></html>"
                        );
                    }
                    else {
                        responseBody.println(
                            "<html><head><title>Guess Number</title><body>" +
                            "<p>Too Low! Guess again</p>" +
                            "<form action='/guess/theguess'>" +
                            "<input type='text' name='ans'>" +
                            "</form></body></html>"
                        );
                    }
                }
                catch( NumberFormatException ex ) {
                    responseBody.println(
                        "<html><head><title>Bad Number</title><body>" +
                        "<p>Bad number! Guess again</p>" +
                        "<form action='/guess/theguess'>" +
                        "<input type='text' name='ans'>" +
                        "</form></body></html>"
                    );
                }
            }
            else {
                responseBody.println(
                    "<html><head><title>Bad Guess Request</title><body>" +
                    "<p>Invalid Request</p>" +
                    "</body></html>"
                );
            }
        }
        responseBody.close();
    }
}
