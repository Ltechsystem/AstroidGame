package dk.sdu.mmmi.cbse.playersystem;

import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.GameKeys;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

import java.util.ServiceLoader;

public class PlayerControlSystem implements IEntityProcessingService {

    private static final double rotationSpeed = 1.5;
    private static final double movementSpeed     = 2.0;

    @Override
    public void process(GameData gameData, World world) {
        for (Entity entity : world.getEntities(Player.class)) {
            GameKeys keys = gameData.getKeys();

            if (keys.isDown(GameKeys.LEFT)) {
                entity.setRotation(entity.getRotation() - rotationSpeed);
            }
            if (keys.isDown(GameKeys.RIGHT)) {
                entity.setRotation(entity.getRotation() + rotationSpeed);
            }
            if (keys.isDown(GameKeys.UP)) {
                double rad = Math.toRadians(entity.getRotation());
                entity.setX(entity.getX() + Math.cos(rad) * movementSpeed);
                entity.setY(entity.getY() + Math.sin(rad) * movementSpeed);
            }
            if (keys.isPressed(GameKeys.SPACE)) {
                shoot(entity, gameData, world);
            }

            wrapPosition(entity, gameData);
        }
    }

    private void shoot(Entity shooter, GameData gameData, World world) {
        ServiceLoader.load(BulletSPI.class).findFirst().ifPresent(spi -> {
            Entity bullet = spi.createBullet(shooter, gameData);
            world.addEntity(bullet);
        });
    }

    private void wrapPosition(Entity e, GameData gd) {
        if (e.getX() < 0)                  e.setX(gd.getDisplayWidth());
        if (e.getX() > gd.getDisplayWidth())  e.setX(0);
        if (e.getY() < 0)                  e.setY(gd.getDisplayHeight());
        if (e.getY() > gd.getDisplayHeight()) e.setY(0);
    }
}