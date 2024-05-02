package org.nemesis.content;

import org.nemesis.game.Projectile;
import org.nemesis.game.Unit;

public class ProjectileFactory implements Factory<Unit, Projectile> {
    private final Projectile.Properties properties;

    public ProjectileFactory(Projectile.Properties properties) {
        this.properties = properties;
    }

    @Override
    public Projectile create(Unit unit) {
        return new Projectile(unit, properties);
    }
}
