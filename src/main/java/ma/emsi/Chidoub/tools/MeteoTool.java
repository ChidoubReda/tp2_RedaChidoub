package ma.emsi.Chidoub.tools;

import dev.langchain4j.agent.tool.Tool;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

public class MeteoTool {

    @Tool("Gives the weather of a city")
    public String donneMeteo(String cityName) {
        HttpURLConnection httpConn = null;

        try {
            // Basic input validation (avoids unnecessary network calls)
            if (cityName == null || cityName.isBlank()) {
                return "Error while retrieving the weather for " + cityName + " : invalid city name";
            }

            // Prepare the city name and build the API URL
            String city = cityName.trim();
            String apiUrl = "https://wttr.in/" + city + "?format=3";

            // Open HTTP connection
            httpConn = (HttpURLConnection) new URI(apiUrl).toURL().openConnection();
            httpConn.setRequestMethod("GET");

            // Add reasonable timeouts and user-agent for stability
            httpConn.setConnectTimeout(8000); // connection timeout
            httpConn.setReadTimeout(8000);    // read timeout
            httpConn.setRequestProperty("User-Agent", "Mozilla/5.0"); // helps prevent rejection by wttr.in

            // Check HTTP response code
            int status = httpConn.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK) {
                // Try to read the error body for more details
                String errorBody = "";
                try (Scanner errScanner = new Scanner(
                        httpConn.getErrorStream() != null ? httpConn.getErrorStream() : httpConn.getInputStream()
                )) {
                    errorBody = errScanner.useDelimiter("\\A").hasNext() ? errScanner.next() : "";
                }
                throw new IOException("HTTP " + status + (errorBody.isEmpty() ? "" : " - " + errorBody));
            }

            // Read the response (same logic as original)
            String responseBody;
            try (Scanner bodyScanner = new Scanner(httpConn.getInputStream())) {
                responseBody = bodyScanner.useDelimiter("\\A").hasNext() ? bodyScanner.next() : "";
            }

            // Keep the exact same output format
            return "Current weather in " + cityName + " : " + responseBody;

        } catch (IOException e) {
            // Keep the same error format
            return "Error while retrieving the weather for " + cityName + " : " + e.getMessage();
        } catch (URISyntaxException e) {
            // Same escalation logic as before
            throw new RuntimeException(e);
        } finally {
            // Explicitly close the connection
            if (httpConn != null) {
                httpConn.disconnect();
            }
        }
    }
}
