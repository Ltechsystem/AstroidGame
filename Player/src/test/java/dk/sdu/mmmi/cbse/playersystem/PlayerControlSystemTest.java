package dk.sdu.mmmi.cbse.playersystem;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.GameKeys;
import dk.sdu.mmmi.cbse.common.data.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerControlSystemTest {

    private PlayerControlSystem system;
    private GameData gameData;
    private World world;
    private Player player;

    @BeforeEach
    void setUp() {
        system   = new PlayerControlSystem();
        gameData = new GameData();
        world    = new World();
        player   = new Player();
        player.setX(400);
        player.setY(400);
        player.setRotation(0);
        player.setRadius(8);
        player.setLife(3);
        player.setPolygonCoordinates(-5, -5, 10, 0, -5, 5);
        world.addEntity(player);
    }

    @Test
    void rotatesLeftWhenLeftKeyHeld() {
        gameData.getKeys().setKey(GameKeys.LEFT, true);
        double before = player.getRotation();
        system.process(gameData, world);
        assertTrue(player.getRotation() < before,
                "rotation should decrease when LEFT key is held");
    }

    @Test
    void rotatesRightWhenRightKeyHeld() {
        gameData.getKeys().setKey(GameKeys.RIGHT, true);
        double before = player.getRotation();
        system.process(gameData, world);
        assertTrue(player.getRotation() > before,
                "rotation should increase when RIGHT key is held");
    }

    @Test
    void movesForwardWhenUpKeyHeld() {
        player.setRotation(0); // facing right
        gameData.getKeys().setKey(GameKeys.UP, true);
        double beforeX = player.getX();
        double beforeY = player.getY();
        system.process(gameData, world);
        assertNotEquals(beforeX, player.getX(),
                "X position should change when thrusting at rotation=0");
    }

    @Test
    void noMovementWhenNoKeyPressed() {
        double beforeX = player.getX();
        double beforeY = player.getY();
        double beforeRot = player.getRotation();
        system.process(gameData, world);
        assertEquals(beforeX,   player.getX(),        0.001, "X should not change");
        assertEquals(beforeY,   player.getY(),        0.001, "Y should not change");
        assertEquals(beforeRot, player.getRotation(), 0.001, "rotation should not change");
    }

    @Test
    void playerWrapsAroundLeftEdge() {
        player.setX(-1);
        system.process(gameData, world);
        assertTrue(player.getX() >= 0, "player should wrap to right side of screen");
    }

    @Test
    void playerWrapsAroundTopEdge() {
        player.setY(-1);
        system.process(gameData, world);
        assertTrue(player.getY() >= 0, "player should wrap to bottom of screen");
    }
}