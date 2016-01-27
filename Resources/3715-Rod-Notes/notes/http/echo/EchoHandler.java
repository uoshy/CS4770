import java.io.IOException;
import java.io.PrintWriter;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Date;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class EchoHandler implements HttpHandler {
    private int counter = 0;

    public void handle(HttpExchange exchange) throws IOException {
        counter++;
        // setup the reply
        Headers responseHeaders = exchange.getResponseHeaders();
        responseHeaders.set("Content-Type", "text/plain");
        exchange.sendResponseHeaders(200, 0);
        PrintWriter responseBody = new PrintWriter( exchange.getResponseBody());

        // report http info
        String requestMethod = exchange.getRequestMethod();
        responseBody.println( "Method=" + requestMethod );
        InetSocketAddress remote = exchange.getRemoteAddress();
        responseBody.println("Remote=" + remote );
        URI uri = exchange.getRequestURI();
        responseBody.println("URI=" + uri );
        responseBody.println("URI path=" + uri.getPath() );
        responseBody.println("URI query=" + uri.getQuery());

        // log request
        System.out.println("Handling: " + (new Date()) + ":" + uri );

        responseBody.println("Headers:" );
        Headers requestHeaders = exchange.getRequestHeaders();
        Set<String> keySet = requestHeaders.keySet();
        Iterator<String> iter = keySet.iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            List values = requestHeaders.get(key);
            responseBody.println( key + " = " + values.toString() );
        }
        responseBody.println( "counter = " + counter );
        responseBody.close();
    }
}
