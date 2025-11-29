package com.example.crm.web;

public class WebLauncher {
    public static void main(String[] args) throws Exception {
        int port = 4567;
        if (args.length>0) {
            try { port = Integer.parseInt(args[0]); } catch (NumberFormatException ignored) {}
        }
        new HttpWebServer().start(port);
    }
}
