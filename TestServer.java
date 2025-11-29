import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.net.InetSocketAddress;
import java.io.OutputStream;
import java.util.concurrent.Executors;

public class TestServer {
    public static void main(String[] args) {
        try {
            System.out.println("Creating server...");
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
            
            server.createContext("/test", new HttpHandler() {
                public void handle(HttpExchange ex) throws java.io.IOException {
                    System.out.println("Received request!");
                    String response = "Hello World!";
                    ex.sendResponseHeaders(200, response.length());
                    OutputStream os = ex.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                }
            });
            
            server.setExecutor(Executors.newFixedThreadPool(4)); // Use explicit thread pool instead of null
            System.out.println("Starting server on port 8080...");
            server.start();
            System.out.println("Server started!");
            System.out.println("Address: " + server.getAddress());
            
            System.out.println("Entering infinite loop. Press Ctrl+C to stop.");
            while (true) {
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            System.err.println("EXCEPTION:");
            e.printStackTrace();
        }
    }
}
