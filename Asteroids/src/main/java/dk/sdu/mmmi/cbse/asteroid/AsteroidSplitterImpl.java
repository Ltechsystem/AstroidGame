package dk.sdu.mmmi.cbse.asteroid;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.asteroids.IAsteroidSplitter;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.World;

import java.util.Random;

/**
 * Splits a parent asteroid into two smaller child asteroids.
 * <p>
 * Each child gets half the parent's radius and flies off in a direction
 * roughly perpendicular to the parent's heading, giving a realistic
 * fragmentation feel.
 */
public class AsteroidSplitterImpl implements IAsteroidSplitter {

    private final Random rng = new Random();

    @Override
    public void createSplitAsteroid(Entity parent, World world) {
        double childRadius = parent.getRadius() / 2.0;

        // Two fragments, launched at ±45° from the parent rotation
        double baseAngle = parent.getRotation();
        createFragment(parent, world, baseAngle + 45,  childRadius);
        createFragment(parent, world, baseAngle - 45, childRadius);
    }

    private void createFragment(Entity parent, World world, double rotation, double radius) {
        Asteroid child = new Asteroid();
        child.setRadius(radius);
        child.setRotation(rotation + (rng.nextDouble() * 30 - 15)); // slight jitter
        child.setX(parent.getX() + (rng.nextDouble() * 10 - 5));
        child.setY(parent.getY() + (rng.nextDouble() * 10 - 5));
        child.setPolygonCoordinates(buildPolygon(radius));
        world.addEntity(child);
    }

    private double[] buildPolygon(double radius) {
        int sides = 6 + rng.nextInt(4);
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