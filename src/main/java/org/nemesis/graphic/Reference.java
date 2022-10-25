package org.nemesis.graphic;

import org.nemesis.game.Entity;

public interface Reference<T extends Entity> {
    public void update(T delta);
}
