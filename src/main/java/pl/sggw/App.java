package pl.sggw;

import pl.sggw.handlers.GetHandler;
import pl.sggw.handlers.PostHandler;
import pl.sggw.handlers.RequestHandler;
import pl.sggw.model.Book;
import pl.sggw.serializers.JSONSerializer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class App {
    private static ConcurrentHashMap<Integer, Book> bookDataBase = new ConcurrentHashMap<>();
    private static final Integer PORT = 8080;
    private static final Boolean CONNECTION = true;
    private static final String RESPONSE_HEADERS = "HTTP/1.1 200 OK\nConnection: close\nContent-Type: text/html\n\n";
    private static final String DATABASE_PATH = "src/main/resources/library.json";
    private static String responseView = "";

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        String loadedJSON = JSONSerializer.readJSON(DATABASE_PATH);
        bookDataBase = JSONSerializer.getBooksConcurrentHashMap(loadedJSON);

        System.out.println("Server: http://localhost:" + PORT);

        while (CONNECTION) {
            listenAndServe(serverSocket);
        }
    }

    public static void serveRequest(Socket request) throws IOException {
        PrintWriter out = new PrintWriter(request.getOutputStream());
        HashMap<String, String> requestHeaders = RequestHandler.getRequestHeaders(request.getInputStream());

        String method = requestHeaders.get("Method");
        String route = requestHeaders.get("Resource");

        if (method.equalsIgnoreCase("POST")) {
            HashMap<String, String> requestBody = RequestHandler.getRequestBody(requestHeaders.get("Body"));
            responseView = PostHandler.returnResource(route, requestBody, bookDataBase);
        } else if (method.equalsIgnoreCase("GET")) {
            responseView = GetHandler.returnResource(route, bookDataBase);
        }

        out.print(RESPONSE_HEADERS);
        out.println(responseView);
        out.flush();
        out.close();
    }

    private static void listenAndServe(ServerSocket serverSocket) throws IOException {
        Socket accepted = serverSocket.accept();

        Thread thread = new Thread(() -> {
            try {
                serveRequest(accepted);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        thread.start();
    }
}
