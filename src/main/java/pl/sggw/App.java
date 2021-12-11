package pl.sggw;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;
import java.util.HashMap;

import static pl.sggw.handlers.RequestHandler.getRequestHeaders;

public class App {
    private static final Integer PORT = 8080;
    private static final Boolean CONNECTION = true;
    private static final String RESPONSE_HEADERS = "HTTP/1.1 200 OK\nConnection: close\nContent-Type: text/html\n\n";

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);

        System.out.println("Server: http://localhost:" + PORT);

        while (CONNECTION) {
            listenAndServe(serverSocket);
        }
    }

    public static void serveRequest(Socket request) throws IOException {
        InputStream inputStream = request.getInputStream();
        OutputStream outputStream = request.getOutputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));

        HashMap<String, String> requestHeaders = getRequestHeaders(inputStream);

        writer.write(RESPONSE_HEADERS);
        writer.write("<head></head><body>Test " + LocalTime.now() + "</body>\n\n");

        writer.flush();
        writer.close();
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
