package org.nemesis.game;

import org.nemesis.content.ContentLoader;
import org.nemesis.content.Factory;
import org.nemesis.content.Identity;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

public class Weapon extends HardPoint {
    private final @Nonnull Properties properties;

    public Weapon(@Nonnull Unit unit, @Nonnull Properties properties) {
        super(unit);
        this.properties = properties;

        setRotation(properties.getInitRotation());
    }

    @SuppressWarnings("unused")
    public static class Properties extends Identity {

        public Properties(@Nonnull String id) {
            super(id);
        }

        @Override
        public void withContentLoader(@Nonnull ContentLoader loader) {
            projectileFactory = loader.getProjectileFactory(projectileId);
        }

        private String projectileId;

        private transient Factory<Weapon, Projectile> projectileFactory;

        @CheckForNull
        public Factory<Weapon, Projectile> getProjectileFactory() {
            return projectileFactory;
        }

        private double reloadSpeed;

        public double getReloadSpeed() {
            return reloadSpeed;
        }

        private boolean rotatable;

        public boolean isRotatable() {
            return rotatable;
        }

        private double initRotation;

        public double getInitRotation() {
            return initRotation;
        }
    }

    @Override
    public void update() {
        if (getUnit().hasTarget() && !isReloading()) {
            if (getUnit().getTarget().getHitPoints() > 0) {
                var creator = getProperties().getProjectileFactory();
                if (creator != null) {
                    creator.create(this);
                    setReloadTime(getProperties().getReloadSpeed());
                }
            } else {
                getUnit().setTarget(null);
            }
        }
        setReloadTime(Math.max(getReloadTime() - 1, 0));
    }

    @Nonnull
    public Properties getProperties() {
        return properties;
    }

    private double rotation;

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public double getRotation() {
        return rotation;
    }

    public double getTransformedRotation() {
        return getRotation() + getUnit().getRotation();
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
}
