package dk.sdu.mmmi.cbse.enemysystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.EntityType;

public class Enemy extends Entity {
    public Enemy() {
        setEntityType(EntityType.ENEMY);
    }
}