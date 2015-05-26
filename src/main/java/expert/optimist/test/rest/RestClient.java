package expert.optimist.test.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public enum RestClient {

    INSTANCE;

    public enum REQUEST_METHOD {

        GET("GET"), POST("POST"), HEAD("HEAD"), OPTIONS("OPTIONS"), PUT("PUT"), DELETE("DELETE"), TRACE("TRACE");

        private String requestMethod;

        REQUEST_METHOD(String requestMethod) {
            this.requestMethod = requestMethod;
        }

        public String getRequestMethod() {
            return requestMethod;
        }
    }

    public String callRESTService(String urlString) {
        Map<String, String> defaultRequestProperties = new HashMap<>();
        defaultRequestProperties.put("Accept", "application/json");
        return callRESTService(urlString, REQUEST_METHOD.GET, defaultRequestProperties);
    }

    public String callRESTService(String urlString, REQUEST_METHOD requestMethod, Map<String, String> requestProperties) {
        HttpURLConnection connection = getHttpURLConnection(urlString);
        try {
            return callRESTService(connection, requestMethod, requestProperties);
        } finally {
            connection.disconnect();
        }
    }

    public String callRESTService(HttpURLConnection connection, REQUEST_METHOD requestMethod, Map<String, String> requestProperties) {
        setRequestMethod(requestMethod, connection);
        for (String requestPropertyKey : requestProperties.keySet()) {
            connection.setRequestProperty(requestPropertyKey, requestProperties.get(requestPropertyKey));
        }

        Integer responseCode = getResponseCode(connection);
        if (responseCode != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + responseCode);
        }

        return getResponse(connection);
    }

    public String getResponse(HttpURLConnection conn) {
        BufferedReader br = new BufferedReader(new InputStreamReader(
                getInputStream(conn)));

        String output;
        StringBuilder wholeOutput = new StringBuilder();
        while ((output = readLine(br)) != null) {
            if (!wholeOutput.toString().isEmpty()) {
                wholeOutput.append(System.lineSeparator());
            }
            wholeOutput.append(output);
        }
        return wholeOutput.toString();
    }

    public String readLine(BufferedReader br) {
        try {
            return br.readLine();
        } catch (IOException e) {
            throw new RuntimeException("Could not read line!");
        }
    }

    public InputStream getInputStream(HttpURLConnection conn) {
        try {
            return conn.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException("Could not return input stream!");
        }
    }

    public Integer getResponseCode(HttpURLConnection conn) {
        try {
            return conn.getResponseCode();
        } catch (IOException e) {
            throw new RuntimeException("Could not return response code!");
        }
    }

    public void setRequestMethod(REQUEST_METHOD requestMethod, HttpURLConnection conn) {
        try {
            conn.setRequestMethod(requestMethod.getRequestMethod());
        } catch (ProtocolException e) {
            throw new RuntimeException("Could not set request method: " + requestMethod.getRequestMethod());
        }
    }

    public HttpURLConnection getHttpURLConnection(String urlString) {
        try {
            return (HttpURLConnection) getURL(urlString).openConnection();
        } catch (IOException e) {
            throw new RuntimeException("Could not open connection for " + urlString);
        }
    }

    public URL getURL(String urlString) {
        try {
            return new URL(urlString);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(urlString + " is not a valid URL!");
        }
    }
}
