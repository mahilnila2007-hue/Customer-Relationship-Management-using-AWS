package com.example.crm.web;

import com.example.crm.model.*;
import com.example.crm.service.*;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class HttpWebServer {
    private final CustomerService customerService;
    private final LeadService leadService;
    private final SaleService saleService;
    private final CampaignService campaignService;
    private final TicketService ticketService;
    private final InteractionService interactionService;
    private final UserService userService;

    public HttpWebServer() {
        System.out.println("Initializing HttpWebServer...");
        try {
            System.out.println("Creating services...");
            customerService = new CustomerService();
            leadService = new LeadService();
            saleService = new SaleService();
            campaignService = new CampaignService();
            ticketService = new TicketService();
            interactionService = new InteractionService();
            userService = new UserService();
            System.out.println("All services created successfully.");
        } catch (Exception e) {
            System.err.println("ERROR creating services:");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void start(int port) throws IOException {
        System.out.println("Creating HttpServer on port " + port + "...");
        InetSocketAddress addr = new InetSocketAddress(port);
        System.out.println("Binding to address: " + addr);
        HttpServer server = HttpServer.create(addr, 0);
        System.out.println("Server created successfully.");
        
        // Test endpoint that doesn't use database
        server.createContext("/test", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String response = "{\"status\":\"OK\",\"message\":\"Server is working!\"}";
                byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, bytes.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(bytes);
                }
            }
        });
        
        server.createContext("/", new StaticHandler());
        System.out.println("Registered static handler for /");
        
        server.createContext("/api/customers", new JsonHandler() {
            @Override
            public void handleJson(HttpExchange exchange) throws IOException {
                if ("GET".equals(exchange.getRequestMethod())) {
                    try { List<Customer> list = customerService.listCustomers(); writeJson(exchange, listToJson(list)); } catch (ServiceException e) { writeError(exchange, 500, e.getMessage()); }
                } else if ("POST".equals(exchange.getRequestMethod())) {
                    Customer c = fromJson(exchange.getRequestBody(), Customer.class);
                    try { Customer created = customerService.createCustomer(c); writeJson(exchange, objectToJson(created), 201); } catch (ServiceException e) { writeError(exchange,400,e.getMessage()); }
                } else { exchange.sendResponseHeaders(405,-1); }
            }
        });

        // Leads
        server.createContext("/api/leads", new JsonHandler(){ @Override public void handleJson(HttpExchange exchange) throws IOException { if ("GET".equals(exchange.getRequestMethod())){ try{writeJson(exchange,listToJson(leadService.listLeads()));}catch(ServiceException e){writeError(exchange,500,e.getMessage());}} else if ("POST".equals(exchange.getRequestMethod())){Lead l = fromJson(exchange.getRequestBody(), Lead.class); try{writeJson(exchange, objectToJson(leadService.createLead(l)),201);}catch(ServiceException e){writeError(exchange,400,e.getMessage());}} else exchange.sendResponseHeaders(405,-1); } });

        // Sales
        server.createContext("/api/sales", new JsonHandler(){ @Override public void handleJson(HttpExchange exchange) throws IOException { if ("GET".equals(exchange.getRequestMethod())){ try{writeJson(exchange,listToJson(saleService.listSales()));}catch(ServiceException e){writeError(exchange,500,e.getMessage());}} else if ("POST".equals(exchange.getRequestMethod())){Sale s = fromJson(exchange.getRequestBody(), Sale.class); try{writeJson(exchange, objectToJson(saleService.createSale(s)),201);}catch(ServiceException e){writeError(exchange,400,e.getMessage());}} else exchange.sendResponseHeaders(405,-1); } });

        // Campaigns
        server.createContext("/api/campaigns", new JsonHandler(){ @Override public void handleJson(HttpExchange exchange) throws IOException { if ("GET".equals(exchange.getRequestMethod())){ try{writeJson(exchange,listToJson(campaignService.listCampaigns()));}catch(ServiceException e){writeError(exchange,500,e.getMessage());}} else if ("POST".equals(exchange.getRequestMethod())){Campaign c = fromJson(exchange.getRequestBody(), Campaign.class); try{writeJson(exchange, objectToJson(campaignService.createCampaign(c)),201);}catch(ServiceException e){writeError(exchange,400,e.getMessage());}} else exchange.sendResponseHeaders(405,-1); } });

        // Tickets
        server.createContext("/api/tickets", new JsonHandler(){ @Override public void handleJson(HttpExchange exchange) throws IOException { if ("GET".equals(exchange.getRequestMethod())){ try{writeJson(exchange,listToJson(ticketService.listTickets()));}catch(ServiceException e){writeError(exchange,500,e.getMessage());}} else if ("POST".equals(exchange.getRequestMethod())){Ticket t = fromJson(exchange.getRequestBody(), Ticket.class); try{writeJson(exchange, objectToJson(ticketService.createTicket(t)),201);}catch(ServiceException e){writeError(exchange,400,e.getMessage());}} else exchange.sendResponseHeaders(405,-1); } });

        // Interactions
        server.createContext("/api/interactions", new JsonHandler(){ @Override public void handleJson(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())){ Interaction i = fromJson(exchange.getRequestBody(), Interaction.class); try{ writeJson(exchange, objectToJson(interactionService.createInteraction(i)),201); } catch(ServiceException e){ writeError(exchange,400,e.getMessage()); } }
            else exchange.sendResponseHeaders(405,-1);
        }});

        // Users
        server.createContext("/api/users", new JsonHandler(){ @Override public void handleJson(HttpExchange exchange) throws IOException { if ("GET".equals(exchange.getRequestMethod())){ try{writeJson(exchange,listToJson(userService.listUsers()));}catch(ServiceException e){writeError(exchange,500,e.getMessage());}} else if ("POST".equals(exchange.getRequestMethod())){User u = fromJson(exchange.getRequestBody(), User.class); try{writeJson(exchange, objectToJson(userService.createUser(u)),201);}catch(ServiceException e){writeError(exchange,400,e.getMessage());}} else exchange.sendResponseHeaders(405,-1); } });

        System.out.println("Setting executor...");
        server.setExecutor(null); // creates a default executor
        System.out.println("Starting server...");
        try {
            server.start();
            System.out.println("server.start() completed");
        } catch (Exception e) {
            System.err.println("ERROR in server.start():");
            e.printStackTrace();
            throw e;
        }
        System.out.println("HTTP server started on port " + port);
        System.out.println("Open http://localhost:" + port + "/ in your browser");
        System.out.println("Server address: " + server.getAddress());
    }

    // Very small JSON helpers (no external libs) — naive implementations for prototype only
    private static String objectToJson(Object o) { return mapToJson(o); }
    private static String listToJson(List<?> list) { StringBuilder sb = new StringBuilder(); sb.append('['); boolean first=true; for (Object o: list){ if (!first) sb.append(','); sb.append(mapToJson(o)); first=false; } sb.append(']'); return sb.toString(); }

    private static String mapToJson(Object o) {
        // naive: reflect getters for simple POJOs
        StringBuilder sb = new StringBuilder(); sb.append('{'); boolean first=true;
        for (var m: o.getClass().getMethods()){
            try{
                if (m.getParameterCount()==0 && m.getName().startsWith("get") && !m.getName().equals("getClass")){
                    String key = Character.toLowerCase(m.getName().charAt(3)) + m.getName().substring(4);
                    Object val = m.invoke(o);
                    String sval = val==null?"null":escapeJson(val.toString());
                    if (!first) sb.append(','); sb.append('"').append(key).append('"').append(':');
                    if (val==null) sb.append("null"); else sb.append('"').append(sval).append('"');
                    first=false;
                }
            }catch(Exception ignore){}
        }
        sb.append('}'); return sb.toString();
    }

    private static String escapeJson(String s){ return s.replace("\\","\\\\").replace("\"","\\\"").replace("\n","\\n").replace("\r","\\r"); }

    private static <T> T fromJson(InputStream in, Class<T> cls) throws IOException {
        String body = new String(in.readAllBytes(), StandardCharsets.UTF_8);
        // extremely naive parser: only supports flat JSON with string/number fields — maps to setters by name
        T instance;
        try { instance = cls.getDeclaredConstructor().newInstance(); } catch (Exception e) { throw new IOException(e); }
        Map<String,String> map = parseFlatJson(body);
        for (var entry: map.entrySet()){
            String key = entry.getKey(); String val = entry.getValue();
            String setter = "set" + Character.toUpperCase(key.charAt(0)) + key.substring(1);
            for (var m: cls.getMethods()){
                if (m.getName().equals(setter) && m.getParameterCount()==1){
                    Class<?> p = m.getParameterTypes()[0];
                    try{
                        if (p==String.class) m.invoke(instance, val);
                        else if (p==Integer.class || p==int.class) m.invoke(instance, Integer.valueOf(val));
                        else if (p==Double.class || p==double.class) m.invoke(instance, Double.valueOf(val));
                        else m.invoke(instance, val);
                    }catch(Exception ignore){}
                }
            }
        }
        return instance;
    }

    private static Map<String,String> parseFlatJson(String s){
        java.util.Map<String,String> m = new java.util.HashMap<>();
        s = s.trim();
        if (s.startsWith("{")) s = s.substring(1);
        if (s.endsWith("}")) s = s.substring(0,s.length()-1);

        StringBuilder key = new StringBuilder();
        StringBuilder val = new StringBuilder();
        boolean inQuotes = false;
        boolean escape = false;
        boolean readingKey = true;

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (escape) {
                if (readingKey) key.append(c); else val.append(c);
                escape = false;
                continue;
            }
            if (c == '\\') { escape = true; continue; }
            if (c == '"') { inQuotes = !inQuotes; continue; }
            if (!inQuotes && c == ':' && readingKey) { readingKey = false; continue; }
            if (!inQuotes && c == ',') {
                String kk = key.toString().trim();
                String vv = val.toString().trim();
                if (kk.startsWith("\"") && kk.endsWith("\"")) kk = kk.substring(1, kk.length()-1);
                if (vv.startsWith("\"") && vv.endsWith("\"")) vv = vv.substring(1, vv.length()-1);
                if (!kk.isEmpty()) m.put(kk, vv);
                key.setLength(0); val.setLength(0); readingKey = true; continue;
            }
            if (readingKey) key.append(c); else val.append(c);
        }
        String kk = key.toString().trim();
        String vv = val.toString().trim();
        if (kk.startsWith("\"") && kk.endsWith("\"")) kk = kk.substring(1, kk.length()-1);
        if (vv.startsWith("\"") && vv.endsWith("\"")) vv = vv.substring(1, vv.length()-1);
        if (!kk.isEmpty()) m.put(kk, vv);
        return m;
    }

    private static void writeJson(HttpExchange exchange, String json) throws IOException { writeJson(exchange, json,200); }
    private static void writeJson(HttpExchange exchange, String json, int status) throws IOException { byte[] bytes = json.getBytes(StandardCharsets.UTF_8); exchange.getResponseHeaders().set("Content-Type","application/json; charset=utf-8"); exchange.sendResponseHeaders(status, bytes.length); try (OutputStream os = exchange.getResponseBody()){ os.write(bytes); } }

    private static void writeError(HttpExchange exchange, int status, String msg) throws IOException { writeJson(exchange, "{\"error\":\""+escapeJson(msg)+"\"}", status); }

    // removed unused overloads

    private abstract static class JsonHandler implements HttpHandler { @Override public void handle(HttpExchange exchange) throws IOException { try { handleJson(exchange); } catch (IOException e){ writeError(exchange,500,e.getMessage()); } } public abstract void handleJson(HttpExchange exchange) throws IOException; }

    private static class StaticHandler implements HttpHandler { 
        @Override 
        public void handle(HttpExchange exchange) throws IOException { 
            System.out.println("StaticHandler: " + exchange.getRequestURI().getPath());
            String path = exchange.getRequestURI().getPath(); 
            if (path.equals("/") || path.equals("/index.html")) path = "/index.html"; 
            InputStream in = HttpWebServer.class.getResourceAsStream("/webroot" + path); 
            if (in==null) { 
                System.out.println("404 Not Found: /webroot" + path);
                exchange.sendResponseHeaders(404,-1); 
                return; 
            } 
            byte[] bytes = in.readAllBytes(); 
            String contentType = path.endsWith(".html")?"text/html":path.endsWith(".js")?"application/javascript":"application/octet-stream"; 
            exchange.getResponseHeaders().set("Content-Type", contentType+"; charset=utf-8"); 
            exchange.sendResponseHeaders(200, bytes.length); 
            try (OutputStream os = exchange.getResponseBody()){ 
                os.write(bytes); 
            }
            System.out.println("Served: " + path + " (" + bytes.length + " bytes)");
        }
    }
}
