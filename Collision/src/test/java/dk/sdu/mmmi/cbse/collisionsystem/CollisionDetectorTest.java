package dk.sdu.mmmi.cbse.collisionsystem;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.bullet.Bullet;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.EntityType;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CollisionDetectorTest {

    private CollisionDetector detector;
    private GameData gameData;
    private World world;

    @BeforeEach
    void setUp() {
        detector = new CollisionDetector();
        gameData = new GameData();
        world    = new World();
    }

    // -----------------------------------------------------------------------
    // Mock-up entities
    // -----------------------------------------------------------------------

    private Asteroid asteroid(double x, double y, double radius) {
        Asteroid a = new Asteroid();
        a.setX(x); a.setY(y); a.setRadius(radius);
        return a;
    }

    private Bullet playerBullet(double x, double y) {
        Bullet b = new Bullet();
        b.setX(x); b.setY(y); b.setRadius(2);
        b.setParentType(EntityType.PLAYER);
        return b;
    }

    private Bullet enemyBullet(double x, double y) {
        Bullet b = new Bullet();
        b.setX(x); b.setY(y); b.setRadius(2);
        b.setParentType(EntityType.ENEMY);
        return b;
    }

    private Entity ship(EntityType type, double x, double y, int life) {
        Entity s = new Entity();
        s.setEntityType(type);
        s.setX(x); s.setY(y); s.setRadius(8);
        s.setLife(life);
        return s;
    }

    // -----------------------------------------------------------------------
    // Tests
    // -----------------------------------------------------------------------

    @Test
    void noCollision_entitiesTooFarApart() {
        Entity a = asteroid(100, 100, 15);
        Entity b = asteroid(300, 300, 15);
        world.addEntity(a);
        world.addEntity(b);

        detector.process(gameData, world);

        assertEquals(2, world.getEntities().size(), "No entities should be removed");
    }

    @Test
    void bulletHitsLargeAsteroid_bothRemoved_scoreIncreased() {
        Asteroid a = asteroid(200, 200, 20);
        Bullet   b = playerBullet(200, 200);
        world.addEntity(a);
        world.addEntity(b);

        int scoreBefore = gameData.getScore();
        detector.process(gameData, world);

        assertFalse(world.getEntities().contains(a), "Asteroid should be removed");
        assertFalse(world.getEntities().contains(b), "Bullet should be removed");
        assertTrue(gameData.getScore() > scoreBefore, "Score should increase");
    }

    @Test
    void bulletHitsSmallAsteroid_bothRemoved_noSplit() {
        Asteroid a = asteroid(200, 200, 3);
        Bullet   b = playerBullet(200, 200);
        world.addEntity(a);
        world.addEntity(b);

        detector.process(gameData, world);

        assertFalse(world.getEntities().contains(a), "Small asteroid should be removed");
        assertFalse(world.getEntities().contains(b), "Bullet should be removed");
    }

    @Test
    void enemyBulletHitsPlayer_playerLosesLife() {
        Entity player = ship(EntityType.PLAYER, 200, 200, 3);
        Bullet bullet = enemyBullet(200, 200);
        world.addEntity(player);
        world.addEntity(bullet);

        detector.process(gameData, world);

        assertFalse(world.getEntities().contains(bullet), "Bullet should be consumed");
        assertEquals(2, player.getLife(), "Player should lose 1 life");
        assertTrue(world.getEntities().contains(player), "Player with life > 0 stays in world");
    }

    @Test
    void enemyBulletHitsPlayerWithOneLife_playerDestroyed() {
        Entity player = ship(EntityType.PLAYER, 200, 200, 1);
        Bullet bullet = enemyBullet(200, 200);
        world.addEntity(player);
        world.addEntity(bullet);

        detector.process(gameData, world);

        assertFalse(world.getEntities().contains(player), "Player with 0 life should be removed");
    }

    @Test
    void playerBulletHitsEnemy_enemyLosesLife() {
        Entity enemy  = ship(EntityType.ENEMY, 200, 200, 2);
        Bullet bullet = playerBullet(200, 200);
        world.addEntity(enemy);
        world.addEntity(bullet);

        detector.process(gameData, world);

        assertEquals(1, enemy.getLife(), "Enemy should lose 1 life");
    }

    @Test
    void playerBulletHitsPlayer_noEffect_friendlyFireIgnored() {
        Entity player = ship(EntityType.PLAYER, 200, 200, 3);
        Bullet bullet = playerBullet(200, 200);
        world.addEntity(player);
        world.addEntity(bullet);

        detector.process(gameData, world);

        assertTrue(world.getEntities().contains(bullet),
                "Friendly bullet should not be consumed");
        assertEquals(3, player.getLife(), "Player life should be unchanged");
    }

    @Test
    void shipCollidesWithAsteroid_playerLosesOneLife_asteroidDestroyed() {
        Entity player = ship(EntityType.PLAYER, 200, 200, 3);
        Asteroid a    = asteroid(200, 200, 15);
        world.addEntity(player);
        world.addEntity(a);

        detector.process(gameData, world);

        assertTrue(world.getEntities().contains(player), "Player with life > 1 should survive");
        assertEquals(2, player.getLife(), "Player should lose 1 life");
        assertFalse(world.getEntities().contains(a), "Asteroid should be destroyed");
    }

    @Test
    void shipCollidesWithAsteroid_lastLife_playerDestroyed() {
        Entity player = ship(EntityType.PLAYER, 200, 200, 1);
        Asteroid a    = asteroid(200, 200, 15);
        world.addEntity(player);
        world.addEntity(a);

        detector.process(gameData, world);

        assertFalse(world.getEntities().contains(player), "Player with 0 life should be removed");
        assertFalse(world.getEntities().contains(a),      "Asteroid should be destroyed");
    }
}