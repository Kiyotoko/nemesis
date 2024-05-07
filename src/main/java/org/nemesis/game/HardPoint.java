package org.nemesis.game;

import javax.annotation.Nonnull;

public abstract class HardPoint implements Entity {
    private final @Nonnull Unit unit;

    protected HardPoint(@Nonnull Unit unit) {
        this.unit = unit;
    }

    @Nonnull
    public Unit getUnit() {
        return unit;
    }
}
