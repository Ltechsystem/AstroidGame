package dk.sdu.mmmi.cbse.common.bullet;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.EntityType;

public class Bullet extends Entity {
    private EntityType parentType;

    public Bullet() {
        setEntityType(EntityType.BULLET);
    }

    public EntityType getParentType() {
        return parentType;
    }

    public void setParentType(EntityType parentType) {
        this.parentType = parentType;
    }
}