package dk.sdu.mmmi.cbse.enemysystem;

import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.EntityType;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

import java.util.Optional;
import java.util.Random;
import java.util.ServiceLoader;

public class EnemyControlSystem implements IEntityProcessingService {

    private static final double movementSpeed  = 0.5;
    private static final int    shootInterval  = 150; // frames between shots

    private final Random rng = new Random();
    private int shootTimer = 0;
    private long nextDirectionChange = 0;

    @Override
    public void process(GameData gameData, World world) {
        while (gameData.pollEnemySpawn()) {
            world.addEntity(spawnEnemy(gameData));
        }

        long now = System.currentTimeMillis();
        if (now >= nextDirectionChange) {
            nextDirectionChange = now + 1000 + rng.nextInt(1000);
            for (Entity entity : world.getEntities(Enemy.class)) {
                entity.setRotation(rng.nextDouble() * 360);
            }
        }

        for (Entity entity : world.getEntities(Enemy.class)) {
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

    private Enemy spawnEnemy(GameData gameData) {
        Enemy e = new Enemy();
        e.setPolygonCoordinates(-5, -5, 10, 0, -5, 5);
        e.setRadius(8);
        e.setLife(2);
        double w = gameData.getDisplayWidth();
        double h = gameData.getDisplayHeight();
        switch (rng.nextInt(4)) {
            case 0 -> { e.setX(rng.nextDouble() * w); e.setY(0); }
            case 1 -> { e.setX(rng.nextDouble() * w); e.setY(h); }
            case 2 -> { e.setX(0);                    e.setY(rng.nextDouble() * h); }
            default-> { e.setX(w);                    e.setY(rng.nextDouble() * h); }
        }
        e.setRotation(rng.nextDouble() * 360);
        return e;
    }

    private void fire(Entity shooter, GameData gameData, World world) {
        Optional<Entity> player = world.getEntities().stream()
                .filter(e -> e.getEntityType() == EntityType.PLAYER)
                .findFirst();
        if (player.isEmpty()) return;

        double dx = player.get().getX() - shooter.getX();
        double dy = player.get().getY() - shooter.getY();
        double aimAngle = Math.toDegrees(Math.atan2(dy, dx)) + (rng.nextDouble() * 10 - 5);

        ServiceLoader.load(BulletSPI.class).findFirst().ifPresent(spi -> {
            double savedRotation = shooter.getRotation();
            shooter.setRotation(aimAngle);
            Entity bullet = spi.createBullet(shooter, gameData);
            shooter.setRotation(savedRotation);
            world.addEntity(bullet);
        });
    }
}