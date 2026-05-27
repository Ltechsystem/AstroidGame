package dk.sdu.mmmi.cbse.playersystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;

public class PlayerPlugin implements IGamePluginService {

    @Override
    public void start(GameData gameData, World world) {
        world.addEntity(createPlayerShip(gameData));
    }

    @Override
    public void stop(GameData gameData, World world) {
        // Remove every Player entity, including any that were spawned at runtime
        for (Entity player : world.getEntities(Player.class)) {
            world.removeEntity(player);
        }
    }

    private Entity createPlayerShip(GameData gameData) {
        Player p = new Player();
        p.setPolygonCoordinates(-5, -5, 10, 0, -5, 5);
        p.setX(gameData.getDisplayWidth() / 2.0);
        p.setY(gameData.getDisplayHeight() / 2.0);
        p.setRadius(8);
        p.setLife(3);
        return p;
    }
}