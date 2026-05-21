package dk.sdu.mmmi.cbse.bulletsystem;

import dk.sdu.mmmi.cbse.common.bullet.Bullet;
import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

import java.util.ArrayList;
import java.util.List;

public class BulletControlSystem implements IEntityProcessingService, BulletSPI {

    private static final double bulletSpeed  = 5.0;
    private static final double bulletOffset = 10.0;

    @Override
    public void process(GameData gameData, World world) {
        List<Entity> toRemove = new ArrayList<>();
        for (Entity bullet : world.getEntities(Bullet.class)) {
            Bullet b = (Bullet) bullet;
            double rad = Math.toRadians(b.getRotation());
            b.setX(b.getX() + Math.cos(rad) * bulletSpeed);
            b.setY(b.getY() + Math.sin(rad) * bulletSpeed);

            if (b.getX() < 0 || b.getX() > gameData.getDisplayWidth()
                    || b.getY() < 0 || b.getY() > gameData.getDisplayHeight()) {
                toRemove.add(b);
            }
        }
        toRemove.forEach(world::removeEntity);
    }

    @Override
    public Entity createBullet(Entity shooter, GameData gameData) {
        Bullet bullet = new Bullet();
        double rad = Math.toRadians(shooter.getRotation());
        bullet.setX(shooter.getX() + Math.cos(rad) * bulletOffset);
        bullet.setY(shooter.getY() + Math.sin(rad) * bulletOffset);
        bullet.setRotation(shooter.getRotation());
        bullet.setRadius(2);
        bullet.setPolygonCoordinates(0, 0, 2, 2, 0, 4, -2, 2);
        bullet.setParentType(shooter.getEntityType());
        return bullet;
    }
}