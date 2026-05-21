package dk.sdu.mmmi.cbse.collisionsystem;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.asteroids.IAsteroidSplitter;
import dk.sdu.mmmi.cbse.common.bullet.Bullet;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.EntityType;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;

import java.util.List;
import java.util.ServiceLoader;

public class CollisionDetector implements IPostEntityProcessingService {

    private static final int astroidScore = 10;

    @Override
    public void process(GameData gameData, World world) {
        List<Entity> entities = List.copyOf(world.getEntities());

        for (int i = 0; i < entities.size(); i++) {
            Entity a = entities.get(i);
            for (int j = i + 1; j < entities.size(); j++) {
                Entity b = entities.get(j);

                if (world.getEntity(a.getID()) == null || world.getEntity(b.getID()) == null) continue;
                if (!overlaps(a, b)) continue;

                // Bullet hits an asteroid
                if (isBullet(a) && isAsteroid(b)) {
                    handleBulletAsteroid((Bullet) a, b, gameData, world);
                } else if (isBullet(b) && isAsteroid(a)) {
                    handleBulletAsteroid((Bullet) b, a, gameData, world);

                // Bullet hits a ship
                } else if (isBullet(a) && isShip(b)) {
                    handleBulletShip((Bullet) a, b, world);
                } else if (isBullet(b) && isShip(a)) {
                    handleBulletShip((Bullet) b, a, world);

                // Ship collides with asteroid
                } else if (isShip(a) && isAsteroid(b)) {
                    world.removeEntity(a);
                    world.removeEntity(b);
                } else if (isShip(b) && isAsteroid(a)) {
                    world.removeEntity(b);
                    world.removeEntity(a);
                }
            }
        }
    }

    private void handleBulletAsteroid(Bullet bullet, Entity asteroid,
                                       GameData gameData, World world) {
        world.removeEntity(bullet);
        if (asteroid.getRadius() > Asteroid.MinSplitRadius) {
            ServiceLoader.load(IAsteroidSplitter.class)
                    .findFirst()
                    .ifPresent(splitter -> splitter.createSplitAsteroid(asteroid, world));
        }
        world.removeEntity(asteroid);
        gameData.addScore(astroidScore);
    }

    private void handleBulletShip(Bullet bullet, Entity ship, World world) {
        EntityType shooterType = bullet.getParentType();
        EntityType shipType    = ship.getEntityType();

        boolean hostile = (shooterType == EntityType.ENEMY   && shipType == EntityType.PLAYER)
                       || (shooterType == EntityType.PLAYER  && shipType == EntityType.ENEMY);

        if (!hostile) return;

        world.removeEntity(bullet);
        ship.reduceLife(1);
        if (ship.getLife() <= 0) {
            world.removeEntity(ship);
        }
    }

    private boolean overlaps(Entity a, Entity b) {
        double dx   = a.getX() - b.getX();
        double dy   = a.getY() - b.getY();
        double dist = Math.sqrt(dx * dx + dy * dy);
        return dist < (a.getRadius() + b.getRadius());
    }

    private boolean isBullet(Entity entity)   { return entity.getEntityType() == EntityType.BULLET;   }
    private boolean isAsteroid(Entity entity) { return entity instanceof Asteroid;                     }
    private boolean isShip(Entity entity)     { return entity.getEntityType() == EntityType.PLAYER
                                               || entity.getEntityType() == EntityType.ENEMY;     }
}