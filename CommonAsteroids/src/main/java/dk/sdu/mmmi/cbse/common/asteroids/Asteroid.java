package dk.sdu.mmmi.cbse.common.asteroids;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.EntityType;

public class Asteroid extends Entity {
    public static final double MinSplitRadius = 15.0;

    public Asteroid() {
        setEntityType(EntityType.ASTEROID);
    }
}