package dk.sdu.mmmi.cbse.enemysystem;

import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

import java.util.Random;
import java.util.ServiceLoader;

public class EnemyControlSystem implements IEntityProcessingService {

    private static final double rotationSpeed  = 1.5;
    private static final double movementSpeed      = 0.5;
    private static final int    shootInterval  = 150; // frames between shots

    private final Random rng = new Random();
    private int shootTimer = 0;

    @Override
    public void process(GameData gameData, World world) {
        for (Entity entity : world.getEntities(Enemy.class)) {
            entity.setRotation(entity.getRotation() + rotationSpeed + rng.nextDouble() - 0.5);

            double rad = Math.toRadians(entity.getRotation());
            entity.setX(entity.getX() + Math.cos(rad) * movementSpeed);
            entity.setY(entity.getY() + Math.sin(rad) * movementSpeed);

            if (entity.getX() < 0)                         entity.setX(gameData.getDisplayWidth());
            if (entity.getX() > gameData.getDisplayWidth()) entity.setX(0);
            if (entity.getY() < 0)                         entity.setY(gameData.getDisplayHeight());
            if (entity.getY() > gameData.getDisplayHeight()) entity.setY(0);

            shootTimer++;
            if (shootTimer >= shootInterval) {
                shootTimer = 0;
                fire(entity, gameData, world);
            }
        }
    }

    private void fire(Entity shooter, GameData gameData, World world) {
        ServiceLoader.load(BulletSPI.class).findFirst().ifPresent(spi -> {
            Entity bullet = spi.createBullet(shooter, gameData);
            world.addEntity(bullet);
        });
    }
}