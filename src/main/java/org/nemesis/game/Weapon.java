package org.nemesis.game;

import org.nemesis.content.ContentLoader;
import org.nemesis.content.Factory;
import org.nemesis.content.Identity;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

public class Weapon extends HardPoint {
    private final Properties properties;

    public Weapon(@Nonnull Unit unit, @Nonnull Properties properties) {
        super(unit);
        this.properties = properties;
    }

    @SuppressWarnings("unused")
    public static class Properties extends Identity {

        public Properties(@Nonnull String id) {
            super(id);
        }

        @Override
        public void withContentLoader(@Nonnull ContentLoader loader) {
            factory = loader.getProjectileFactory(projectileId);
        }

        private String projectileId;

        private transient Factory<Unit, Projectile> factory;

        @CheckForNull
        public Factory<Unit, Projectile> getFactory() {
            return factory;
        }
        private double reloadSpeed;

        public double getReloadSpeed() {
            return reloadSpeed;
        }
    }

    @Override
    public void update() {
        shoot();
    }

    public void shoot() {
        if (getUnit().hasTarget() && !isReloading()) {
            if (getUnit().getTarget().getHitPoints() > 0) {
                Factory<Unit, Projectile> creator = getProperties().getFactory();
                if (creator != null) {
                    creator.create(getUnit());
                    setReloadTime(getProperties().getReloadSpeed());
                }
            } else {
                getUnit().setTarget(null);
            }
        }
        setReloadTime(Math.max(getReloadTime() - 1, 0));
    }

    private double reloadTime = 0;

    public boolean isReloading() {
        return reloadTime > 0;
    }

    public void setReloadTime(double reloadTime) {
        this.reloadTime = reloadTime;
    }

    public double getReloadTime() {
        return reloadTime;
    }

    public Properties getProperties() {
        return properties;
    }
}
