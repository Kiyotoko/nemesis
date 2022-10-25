package org.nemesis.game;

import java.util.Collection;

public interface Parent extends Entity {
    @Override
    default void update(double deltaT) {
        for (Entity entity : getChildren()) {
            entity.update(deltaT);
        }
    }

    public Collection<? extends Entity> getChildren();
}
