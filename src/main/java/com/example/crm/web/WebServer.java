package com.example.crm.web;

/**
 * Lightweight wrapper to start the built-in HttpWebServer.
 */
public class WebServer {
    public static void main(String[] args) throws Exception {
        int port = 4567;
        if (args.length>0) {
            try { port = Integer.parseInt(args[0]); } catch (NumberFormatException ignored) {}
        }
        
        try {
            new HttpWebServer().start(port);
        } catch (Exception e) {
            System.err.println("FATAL ERROR starting server:");
            e.printStackTrace();
            System.exit(1);
        }
        
        // Keep the server running
        System.out.println("Server is running. Press Ctrl+C to stop.");
        Thread.currentThread().join();
    }
}
