package utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class RequestUtils {
    private static final Logger LOGGER = LogManager.getLogger(RequestUtils.class);

    private RequestUtils(){}

    public static HttpResponse httpGet(String url) {
        CloseableHttpClient httpClient = null;

        if (url.contains("|")) url = url.replace("|", "%7C");

        try {
            HttpGet request = new HttpGet(url);
            request.addHeader("accept", "application/json");
            httpClient = HttpClients.createDefault();
            return httpClient.execute(request);
        } catch (IOException ioe) {
            throw new IllegalArgumentException("Exception on connection with pool web service" + ioe);
        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException ioe) {
                LOGGER.error("IOException: " + ioe);
            }
        }
    }

    public static Map<String, Integer> getStatusCode(String url) {
        Map<String, Integer> status = new HashMap<>();
        status.put(url, httpGet(url).getStatusLine().getStatusCode());
        return status;
    }

    private static String getContent(HttpResponse response) throws IOException {
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new TestException("Failed : HTTP error code : "
                    + response.getStatusLine().getStatusCode());
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {
            String output;
            StringBuilder result = new StringBuilder();
            while ((output = br.readLine()) != null) {
                result.append(output);
            }
            return result.toString();
        }
    }

}
