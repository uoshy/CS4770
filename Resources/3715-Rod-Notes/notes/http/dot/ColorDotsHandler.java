import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import java.util.Date;
import java.net.URI;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Color;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class ColorDotsHandler implements HttpHandler {
    private int index = 0;
    private Color[] colours = {Color.RED, Color.GREEN, Color.BLUE};

    public void handle(HttpExchange exchange) throws IOException {
        URI uri = exchange.getRequestURI();
        // log request
        System.out.println("Handling: " + (new Date()) + ":" + uri );
        String path = uri.getPath();
        if ( path.equals("/dotpage") ) {
            Headers responseHeaders = exchange.getResponseHeaders();
            responseHeaders.set("Content-Type", "text/html");
            exchange.sendResponseHeaders(200, 0);
            PrintWriter page = new PrintWriter( exchange.getResponseBody());
            page.println(
                "<html><head><title>Image</title><body>" +
                "<a href='http://localhost:8080/dotpage'>" +
                "<img src='http://localhost:8080/dotpage/dot'>" +
                "</a>" +
                "</body></html>"
            );
            page.close();
        }
        else {
            Headers responseHeaders = exchange.getResponseHeaders();
            responseHeaders.set("Content-Type", "image/jpg");
            exchange.sendResponseHeaders(200, 0);
            BufferedImage img =
                new BufferedImage(640,480, BufferedImage.TYPE_3BYTE_BGR);
            Graphics2D g2d = img.createGraphics();
            g2d.setPaint( Color.white );
            g2d.fillRect( 0, 0, 640, 480 );
            g2d.setPaint( colours[ index] );
            g2d.fillOval( 90, 10, 460, 460 ); 
            OutputStream out = exchange.getResponseBody();
            ImageIO.write(img, "jpg", out );
            out.flush();
            out.close();
            index++;
            if ( index >= colours.length ) {
                index = 0;
            }
        }
    }
}
