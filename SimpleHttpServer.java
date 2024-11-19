import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * SimpleHttpServer.java
 *
 * A simple multithreaded HTTP server implementation in Java.
 *
 * Author: Pratik Merekar
 * Email: psmerekar@gmail.com
 * Date: 2004-11-19
 *
 * Description:
 * This program implements a basic HTTP server that listens on a specified port
 * and serves routes like "/", "/time", "/health", and more. It supports multithreading
 * to handle multiple client requests simultaneously.
 *
 * Usage:
 * Run the program, and the server will start on the configured port (default: 8080).
 * Access the server via a browser or HTTP client.
 *
 * License:
 * MIT License
 */

public class SimpleHttpServer {
    private final int port;
    private final Map<String, RequestHandler> routes;
    private boolean running;
    private final ExecutorService executorService;

    public SimpleHttpServer(int port) {
        this.port = port;
        this.routes = new HashMap<>();
        this.executorService = Executors.newFixedThreadPool(10);
        setupDefaultRoutes();
    }

    @FunctionalInterface
    public interface RequestHandler {
        String handle(String request) throws Exception;
    }

    public void start() {
        running = true;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            String GREEN = "\u001B[32m";
            System.out.println(GREEN + "Server started on port " + port);

            while (running) {
                Socket clientSocket = serverSocket.accept();
                executorService.submit(() -> handleClient(clientSocket));
            }
        } catch (Exception e) {
            System.err.println("Server error: " + e.getMessage());
        } finally {
            executorService.shutdown();
        }
    }


    private void handleClient(Socket clientSocket) {
        try (
                clientSocket;
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            String requestLine = in.readLine();
            if (requestLine == null) return;

            String[] requestParts = requestLine.split(" ");
            String method = requestParts[0];
            String path = requestParts[1];

            System.out.println("Request: "+requestLine);
            String line;
            while ((line = in.readLine()) != null && !line.isEmpty()) {
                System.out.println(line);
            }

            String response = routes.getOrDefault(path, req -> createResponse(
                    "404 Not Found",
                    "text/plain",
                    "404 - Page not found"
            )).handle(requestLine);

            out.print(response);
            out.flush();

        } catch (Exception e) {
            System.err.println("Error handling client: " + e.getMessage());
        }
    }

    private String createHtmlResponse(String body) {
        return createResponse("200 OK", "text/html", body);
    }

    private String createJsonResponse(String json) {
        return createResponse("200 OK", "application/json", json);
    }

    private String createResponse(String status, String contentType, String body) {
        return String.format(
                "HTTP/1.1 %s\r\n" +
                        "Content-Type: %s\r\n" +
                        "Content-Length: %d\r\n" +
                        "Server: SimpleHttpServer/1.0\r\n" +
                        "Connection: close\r\n" +
                        "\r\n%s",
                status, contentType, body.length(), body
        );
    }
    public void addRoute(String path, RequestHandler handler) {
        routes.put(path, handler);
    }

    private void setupDefaultRoutes() {
        routes.put("/", (request) -> {
            LocalDateTime now = LocalDateTime.now();
            String formattedTime = now.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
            return createHtmlResponse(
                    "<h1>Hello World! Welcome to the java http server</h1>" +
                            "<p>Current time: " + formattedTime + "</p>" +
                            "<p>Try these routes:</p>" +
                            "<ul>" +
                            "<li><a href='/time'>/time</a> - Get current time in JSON</li>" +
                            "<li><a href='/health'>/health</a> - Server health check</li>" +
                            "<li><a href='/stats'>/stats</a> - Server statistics</li>" +
                            "</ul>"
            );
        });

        routes.put("/time", (request) -> {
            LocalDateTime now = LocalDateTime.now();
            String time = now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            return createJsonResponse("{\"time\": \"" + time + "\"}");
        });

        routes.put("/health", (request) ->
                createJsonResponse("{\"status\": \"healthy\", \"uptime\": \"" +
                        System.currentTimeMillis() + "\"}"));

        routes.put("/stats", (request) ->
                createJsonResponse("{\"activeThreads\": " +
                        Thread.activeCount() + ", \"freeMemory\": " +
                        Runtime.getRuntime().freeMemory() + "}"));
    }

    public static void main(String[] args) {
        SimpleHttpServer server = new SimpleHttpServer(8080);

        server.addRoute("/hello", (request) ->
                server.createHtmlResponse("<h1>Hello, World!</h1>"));

        server.start();
    }
}

