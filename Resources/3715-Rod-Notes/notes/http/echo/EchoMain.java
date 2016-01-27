import java.io.IOException;

import java.net.InetSocketAddress;

import java.util.concurrent.Executors;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class EchoMain {

    public static void main(String[] args) throws IOException {
        InetSocketAddress addr = new InetSocketAddress(8080);
        HttpServer server = HttpServer.create(addr, 5); // backlog is 5

        // route on /echo and /favicon.ico
        server.createContext("/jetty/requests/auth", new EchoHandler());
        server.createContext("/jetty/echo", new EchoHandler());
        server.createContext("/jetty/favicon.ico", new FaviconHandler());
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();
        System.out.println("Server is listening on port 8080" );
    }
}
