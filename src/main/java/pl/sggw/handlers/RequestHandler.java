package pl.sggw.handlers;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class RequestHandler {
    private static String decoding(String urlComponent)
            throws UnsupportedEncodingException {
        return URLDecoder.decode(urlComponent, StandardCharsets.UTF_8.name());
    }

    public static HashMap<String, String> getRequestHeaders(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        HashMap<String, String> headers = new HashMap<>();

        String line = reader.readLine();
        String[] splits = line.split(" ");

        headers.put("Method", splits[0]);
        headers.put("Resource", splits[1].substring(1));

        distinctPutKeyValue(reader, headers);

        if (headers.get("Method").equalsIgnoreCase("Post")) {
            getBodyHeader(reader, headers);
        }

        return headers;
    }

    private static void getBodyHeader(BufferedReader reader, HashMap<String, String> headers) throws IOException {
        StringBuilder bodyHeader = new StringBuilder();
        int length = Integer.parseInt(headers.get("Content-Length"));
        int temp;
        for (int i = 0; i < length; i++) {
            temp = reader.read();
            bodyHeader.append((char) temp);
        }
        headers.put("Body", bodyHeader.toString());
    }

    private static void distinctPutKeyValue(BufferedReader reader, HashMap<String, String> headers) throws IOException {
        String line;
        while (!(line = reader.readLine()).equals("")) {
            String[] keyValuePair = line.split(":", 2);
            headers.put(keyValuePair[0].trim(), keyValuePair[1].trim());
        }
    }

    public static HashMap<String, String> getRequestBody(String body)
            throws IOException {
        HashMap<String, String> requestBody = new HashMap<>();

        if (body != null) {
            String[] parametersBefore = body.split("[&]");

            for (String parameter : parametersBefore) {

                String[] parameterAfter = parameter.split("=", 2);
                String parameterName = decoding(parameterAfter[0]);
                String parameterValue = "";

                parameterValue = getRequestParameterValue(parameterAfter);

                requestBody.put(parameterName, parameterValue);
            }
        }

        return requestBody;
    }

    private static String getRequestParameterValue(String[] requestParameter) throws UnsupportedEncodingException {
        String parameterValue;
        if (requestParameter.length > 1) {
            parameterValue = decoding(requestParameter[1]);
        } else parameterValue = null;
        return parameterValue;
    }

    public static HashMap<String, String> urlParameters(String url) throws IOException {
        return getRequestBody(url);
    }
}
