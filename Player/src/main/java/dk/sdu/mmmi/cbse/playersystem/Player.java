package dk.sdu.mmmi.cbse.playersystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.EntityType;

public class Player extends Entity {
    public Player() {
        setEntityType(EntityType.PLAYER);
    }
}