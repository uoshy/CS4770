import java.io.IOException;

import java.net.InetSocketAddress;

import java.util.concurrent.Executors;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class HttpDemo1 {

    public static void main(String[] args) throws IOException {
        InetSocketAddress addr = new InetSocketAddress(8080);
        HttpServer server = HttpServer.create(addr, 0);

        server.createContext("/echo", new EchoHandler());
        server.createContext("/favicon.ico", new FaviconHandler());
        server.createContext("/guess", new GuessHandler());
        server.createContext("/dotpage", new ColorDotsHandler());
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();
        System.out.println("Server is listening on port 8080" );
    }
}
