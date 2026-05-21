package dk.sdu.mmmi.cbse.scoreservice;

import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ScoreRepository {

    private final Map<String, Score> scores = new ConcurrentHashMap<>();

    public Collection<Score> findAll() {
        return scores.values();
    }

    public Score findByName(String playerName) {
        return scores.get(playerName);
    }

    public Score save(String playerName, int points) {
        Score score = scores.computeIfAbsent(playerName, name -> new Score(name, 0));
        score.setPoints(points);
        return score;
    }
}