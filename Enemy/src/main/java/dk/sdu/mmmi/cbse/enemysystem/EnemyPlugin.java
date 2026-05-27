package dk.sdu.mmmi.cbse.enemysystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;

import java.util.Random;

public class EnemyPlugin implements IGamePluginService {

    private final Random rng = new Random();

    @Override
    public void start(GameData gameData, World world) {
        world.addEntity(createEnemy(gameData));
    }

    @Override
    public void stop(GameData gameData, World world) {
        // Remove every Enemy entity, including any that were spawned at runtime
        for (Entity enemy : world.getEntities(Enemy.class)) {
            world.removeEntity(enemy);
        }
    }

    private Entity createEnemy(GameData gameData) {
        Enemy e = new Enemy();
        e.setPolygonCoordinates(-5, -5, 10, 0, -5, 5);
        e.setRadius(8);
        e.setLife(2);

        int edge = rng.nextInt(4);
        switch (edge) {
            case 0 -> { e.setX(rng.nextDouble() * gameData.getDisplayWidth());  e.setY(0); }
            case 1 -> { e.setX(rng.nextDouble() * gameData.getDisplayWidth());  e.setY(gameData.getDisplayHeight()); }
            case 2 -> { e.setX(0);                                              e.setY(rng.nextDouble() * gameData.getDisplayHeight()); }
            default-> { e.setX(gameData.getDisplayWidth());                     e.setY(rng.nextDouble() * gameData.getDisplayHeight()); }
        }
        e.setRotation(rng.nextDouble() * 360);
        return e;
    }
}