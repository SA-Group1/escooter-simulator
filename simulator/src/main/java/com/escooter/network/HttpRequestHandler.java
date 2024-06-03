package com.escooter.network;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.time.Duration;

public class HttpRequestHandler {

    private final HttpClient httpClient;

    public HttpRequestHandler() {
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();
    }

    public String sendHttpRequest(String urlString, String method, String jsonInputString) throws Exception {
        // Build the URI
        URI uri = new URI(urlString);

        // Create the HttpRequest builder
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(uri)
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json");

        // Add the method and body
        if (method.equalsIgnoreCase("POST") || method.equalsIgnoreCase("PUT")) {
            requestBuilder.method(method, BodyPublishers.ofString(jsonInputString));
        } else {
            requestBuilder.method(method, BodyPublishers.noBody());
        }

        // Build the HttpRequest
        HttpRequest request = requestBuilder.build();

        // Send the request and get the response
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Check the status code
        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed: HTTP error code: " + response.statusCode());
        }

        return response.body();
    }
}
