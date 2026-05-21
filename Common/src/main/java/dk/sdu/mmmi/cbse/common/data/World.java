package dk.sdu.mmmi.cbse.common.data;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class World {

    private final Map<String, Entity> entityMap = new ConcurrentHashMap<>();

    public String addEntity(Entity entity) {
        entityMap.put(entity.getID(), entity);
        return entity.getID();
    }

    public void removeEntity(Entity entity) {
        entityMap.remove(entity.getID());
    }

    public void removeEntity(String id) {
        entityMap.remove(id);
    }

    public Collection<Entity> getEntities() {
        return entityMap.values();
    }

    @SafeVarargs
    public final Collection<Entity> getEntities(Class<? extends Entity>... entityTypes) {
        return entityMap.values().stream()
                .filter(e -> {
                    for (Class<? extends Entity> type : entityTypes) {
                        if (type.isInstance(e)) return true;
                    }
                    return false;
                })
                .toList();
    }

    public Entity getEntity(String id) {
        return entityMap.get(id);
    }
}