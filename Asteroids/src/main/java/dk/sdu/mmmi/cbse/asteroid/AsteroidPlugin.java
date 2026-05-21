package dk.sdu.mmmi.cbse.asteroid;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AsteroidPlugin implements IGamePluginService {

    private static final int    initialCount  = 4;
    private static final double minRadius     = 16.0;
    private static final double maxRadius     = 28.0;

    private final List<Entity> asteroids = new ArrayList<>();
    private final Random rng = new Random();

    @Override
    public void start(GameData gameData, World world) {
        for (int i = 0; i < initialCount; i++) {
            Entity a = createAsteroid(gameData);
            asteroids.add(a);
            world.addEntity(a);
        }
    }

    @Override
    public void stop(GameData gameData, World world) {
        for (Entity a : world.getEntities(Asteroid.class)) {
            world.removeEntity(a);
        }
        asteroids.clear();
    }

    private Entity createAsteroid(GameData gameData) {
        Asteroid a = new Asteroid();
        double radius = minRadius + rng.nextDouble() * (maxRadius - minRadius);
        a.setRadius(radius);
        a.setPolygonCoordinates(buildPolygon(radius));
        a.setRotation(rng.nextDouble() * 360);

        // Random position away from screen center to avoid spawning on the player
        double x, y;
        do {
            x = rng.nextDouble() * gameData.getDisplayWidth();
            y = rng.nextDouble() * gameData.getDisplayHeight();
        } while (distanceToCentre(x, y, gameData) < 150);

        a.setX(x);
        a.setY(y);
        return a;
    }

    private double[] buildPolygon(double radius) {
        int sides = 8 + rng.nextInt(4); // 8-11 sides for irregular look
        double[] coords = new double[sides * 2];
        for (int i = 0; i < sides; i++) {
            double angle = (2 * Math.PI / sides) * i;
            double r = radius * (0.8 + rng.nextDouble() * 0.4);
            coords[i * 2]     = Math.cos(angle) * r;
            coords[i * 2 + 1] = Math.sin(angle) * r;
        }
        return coords;
    }

    private double distanceToCentre(double x, double y, GameData gd) {
        double cx = gd.getDisplayWidth() / 2.0;
        double cy = gd.getDisplayHeight() / 2.0;
        return Math.sqrt((x - cx) * (x - cx) + (y - cy) * (y - cy));
    }
}
