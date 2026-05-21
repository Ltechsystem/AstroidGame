package dk.sdu.mmmi.cbse.asteroid;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

import java.util.Random;

public class AsteroidProcessor implements IEntityProcessingService {

    private static final double speed     = 0.6;
    private static final double minRadius = 16.0;
    private static final double maxRadius = 28.0;

    private final Random rng = new Random();

    @Override
    public void process(GameData gameData, World world) {
        while (gameData.pollAsteroidSpawn()) {
            world.addEntity(spawnAtEdge(gameData));
        }

        for (Entity asteroid : world.getEntities(Asteroid.class)) {
            double rad = Math.toRadians(asteroid.getRotation());
            asteroid.setX(asteroid.getX() + Math.cos(rad) * speed);
            asteroid.setY(asteroid.getY() + Math.sin(rad) * speed);

            double w = gameData.getDisplayWidth();
            double h = gameData.getDisplayHeight();
            if (asteroid.getX() < 0)  asteroid.setX(w);
            if (asteroid.getX() > w)  asteroid.setX(0);
            if (asteroid.getY() < 0)  asteroid.setY(h);
            if (asteroid.getY() > h)  asteroid.setY(0);
        }
    }

    private Asteroid spawnAtEdge(GameData gameData) {
        Asteroid a = new Asteroid();
        double radius = minRadius + rng.nextDouble() * (maxRadius - minRadius);
        a.setRadius(radius);
        a.setPolygonCoordinates(buildPolygon(radius));
        a.setRotation(rng.nextDouble() * 360);

        double w = gameData.getDisplayWidth();
        double h = gameData.getDisplayHeight();
        switch (rng.nextInt(4)) {
            case 0 -> { a.setX(rng.nextDouble() * w); a.setY(0); }
            case 1 -> { a.setX(rng.nextDouble() * w); a.setY(h); }
            case 2 -> { a.setX(0);                    a.setY(rng.nextDouble() * h); }
            default-> { a.setX(w);                    a.setY(rng.nextDouble() * h); }
        }
        return a;
    }

    private double[] buildPolygon(double radius) {
        int sides = 8 + rng.nextInt(4);
        double[] coords = new double[sides * 2];
        for (int i = 0; i < sides; i++) {
            double angle = (2 * Math.PI / sides) * i;
            double r = radius * (0.8 + rng.nextDouble() * 0.4);
            coords[i * 2]     = Math.cos(angle) * r;
            coords[i * 2 + 1] = Math.sin(angle) * r;
        }
        return coords;
    }
}