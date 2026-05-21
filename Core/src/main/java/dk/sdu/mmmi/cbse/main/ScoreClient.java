package dk.sdu.mmmi.cbse.main;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ScoreClient {

    private static final String BASE_URL    = "http://localhost:8080/scores";
    private static final String PLAYER_NAME = "player1";

    private final HttpClient client = HttpClient.newHttpClient();
    private int lastPostedScore = -1;

    public void postScore(int score) {
        if (score == lastPostedScore) return;
        lastPostedScore = score;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/" + PLAYER_NAME + "?points=" + score))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();
            client.sendAsync(request, HttpResponse.BodyHandlers.discarding());
        } catch (Exception ignored){}
    }
}