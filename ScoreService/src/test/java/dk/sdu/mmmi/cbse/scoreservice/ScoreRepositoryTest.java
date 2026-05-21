package dk.sdu.mmmi.cbse.scoreservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class ScoreRepositoryTest {

    private ScoreRepository repository;

    @BeforeEach
    void setUp() {
        repository = new ScoreRepository();
    }

    @Test
    void findAll_returnsEmptyCollection_whenNoScoresSaved() {
        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    void save_createsNewScoreWithCorrectValues() {
        Score result = repository.save("lars", 42);

        assertEquals("lars", result.getPlayerName());
        assertEquals(42, result.getPoints());
    }

    @Test
    void save_updatesPoints_whenCalledAgainForSamePlayer() {
        repository.save("bob", 10);
        Score result = repository.save("bob", 99);

        assertEquals(99, result.getPoints());
    }

    @Test
    void save_doesNotCrossContaminatePlayerScores() {
        repository.save("lars", 10);
        repository.save("bob", 20);

        assertEquals(10, repository.findByName("lars").getPoints());
        assertEquals(20, repository.findByName("bob").getPoints());
    }

    @Test
    void findAll_returnsAllSavedScores() {
        repository.save("lars", 10);
        repository.save("bob", 20);

        Collection<Score> all = repository.findAll();
        assertEquals(2, all.size());
    }

    @Test
    void findByName_returnsNull_whenPlayerNotFound() {
        assertNull(repository.findByName("unknown"));
    }

    @Test
    void findByName_returnsCorrectScore_whenPlayerExists() {
        repository.save("karl", 55);

        Score found = repository.findByName("karl");
        assertNotNull(found);
        assertEquals(55, found.getPoints());
    }
}