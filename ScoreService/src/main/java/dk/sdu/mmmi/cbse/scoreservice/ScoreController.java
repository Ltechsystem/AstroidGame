package dk.sdu.mmmi.cbse.scoreservice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/scores")
public class ScoreController {

    private final ScoreRepository repository;

    public ScoreController(ScoreRepository repository) {
        this.repository = repository;
    }

    /** Returns all stored scores. */
    @GetMapping
    public Collection<Score> getAllScores() {
        return repository.findAll();
    }

    /** Returns the score for the given player or 404 if not found. */
    @GetMapping("/{name}")
    public ResponseEntity<Score> getScore(@PathVariable String name) {
        Score score = repository.findByName(name);
        return score != null
                ? ResponseEntity.ok(score)
                : ResponseEntity.notFound().build();
    }

    @PostMapping("/{name}")
    public Score postScore(@PathVariable String name,
                           @RequestParam int    points) {
        return repository.save(name, points);
    }
}