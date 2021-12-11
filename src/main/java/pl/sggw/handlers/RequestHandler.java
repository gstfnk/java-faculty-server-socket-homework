package pl.sggw.handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class RequestHandler {
    private static final String REQUEST_HEADER_METHOD = "Method";
    private static final String REQUEST_HEADER_RESOURCE = "Resource";

    public static HashMap<String, String> getRequestHeaders(InputStream inputStream) throws IOException {
        HashMap<String, String> headers = new HashMap<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line = reader.readLine();
        String[] splits = line.split(" ");

        headers.put(REQUEST_HEADER_METHOD, splits[0]);
        headers.put(REQUEST_HEADER_RESOURCE, splits[1].substring(1));

        while ((line = reader.readLine()) != null && line.length() > 0) {
            String[] key = line.split(": ", 2);
            headers.put(key[0], key[1]);
        }

        System.out.println(getRequestHeaderMethod(headers));
        System.out.println(getRequestHeaderResource(headers));

        return headers;
    }

    public static String getRequestHeaderMethod(HashMap<String, String> headers) {
        return headers.get(REQUEST_HEADER_METHOD);
    }

    public static String getRequestHeaderResource(HashMap<String, String> headers) {
        return headers.get(REQUEST_HEADER_RESOURCE);
    }
}
