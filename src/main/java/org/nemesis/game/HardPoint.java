package org.nemesis.game;

import javax.annotation.Nonnull;

public abstract class HardPoint {
    private final @Nonnull Unit unit;

    protected HardPoint(@Nonnull Unit unit) {
        this.unit = unit;
    }

    public abstract void update();

    @Nonnull
    public Unit getUnit() {
        return unit;
    }
}
