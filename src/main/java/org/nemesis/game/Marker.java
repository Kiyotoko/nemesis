package org.nemesis.game;

import javax.annotation.Nonnull;

public abstract class Marker extends GameObject {
    protected Marker(@Nonnull Game game) {
        super(game);
    }

    @Override
    public void destroy() {
        // Marker objects are indestructible
        throw new UnsupportedOperationException();
    }
}
