package server;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {

    private final String apiToken;
    private final HttpClient client;
    private final HttpResponse.BodyHandler<String> handler;
    private HttpResponse<String> response;

    public KVTaskClient(URI uri) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        client = HttpClient.newHttpClient();
        handler = HttpResponse.BodyHandlers.ofString();
        response = client.send(request, handler);
        apiToken = response.body();
    }

    private void put(String key, String json) throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8078/save/" + key + "?" + "API_TOKEN=" + apiToken);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        client.send(request, handler);
    }

    private String load(String key) throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8078/load/" + key + "?" + "API_TOKEN=" + apiToken);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(request, handler);
        return response.body();
    }
}
