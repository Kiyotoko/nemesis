package org.nemesis.game;

import java.util.Set;

public interface Parent extends Entity {
    @Override
    default void update(double deltaT) {
        for (Entity entity : getChildren()) {
            entity.update(deltaT);
        }
    }

    public Set<? extends Entity> getChildren();
}
