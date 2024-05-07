package org.nemesis.content;

import org.nemesis.game.*;

public final class Factories {
    private Factories() {
        throw new UnsupportedOperationException();
    }

    public static Factory<Unit, Weapon> createFactory(Weapon.Properties properties) {
        return unit -> new Weapon(unit, properties);
    }

    public static Factory<Unit, Engine> createFactory(Engine.Properties properties) {
        return unit -> new Engine(unit, properties);
    }

    public static Factory<Player, Unit> createFactory(Unit.Properties properties) {
        return player -> new Unit(player, properties);
    }

    public static Factory<Unit, Projectile> createFactory(Projectile.Properties properties) {
        return unit -> new Projectile(unit, properties);
    }
}
