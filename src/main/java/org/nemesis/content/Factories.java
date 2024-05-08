package org.nemesis.content;

import org.nemesis.game.*;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

public final class Factories {
    private Factories() {
        throw new UnsupportedOperationException();
    }

    @CheckReturnValue
    @Nonnull
    public static Factory<Unit, Weapon> createFactory(@Nonnull Weapon.Properties properties) {
        return unit -> new Weapon(unit, properties);
    }

    @CheckReturnValue
    @Nonnull
    public static Factory<Unit, Engine> createFactory(@Nonnull Engine.Properties properties) {
        return unit -> new Engine(unit, properties);
    }

    @CheckReturnValue
    @Nonnull
    public static Factory<Player, Unit> createFactory(@Nonnull Unit.Properties properties) {
        return player -> new Unit(player, properties);
    }

    @CheckReturnValue
    @Nonnull
    public static Factory<Weapon, Projectile> createFactory(@Nonnull Projectile.Properties properties) {
        return unit -> new Projectile(unit, properties);
    }
}
