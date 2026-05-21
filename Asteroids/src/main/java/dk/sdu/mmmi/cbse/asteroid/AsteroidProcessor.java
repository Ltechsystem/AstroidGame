package dk.sdu.mmmi.cbse.asteroid;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

public class AsteroidProcessor implements IEntityProcessingService {

    private static final double speed = 0.6;

    @Override
    public void process(GameData gameData, World world) {
        for (Entity asteroid : world.getEntities(Asteroid.class)) {
            double rad = Math.toRadians(asteroid.getRotation());
            asteroid.setX(asteroid.getX() + Math.cos(rad) * speed);
            asteroid.setY(asteroid.getY() + Math.sin(rad) * speed);

            // Screen wrap
            double w = gameData.getDisplayWidth();
            double h = gameData.getDisplayHeight();
            if (asteroid.getX() < 0)  asteroid.setX(w);
            if (asteroid.getX() > w)  asteroid.setX(0);
            if (asteroid.getY() < 0)  asteroid.setY(h);
            if (asteroid.getY() > h)  asteroid.setY(0);
        }
    }
}