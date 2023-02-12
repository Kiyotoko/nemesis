package org.nemesis.game;

import java.io.File;
import java.util.function.Function;

import javax.annotation.Nonnull;

import org.nemesis.game.FactoryResource.ProjectileResource;
import org.nemesis.game.FactoryResource.UnitResource;

public abstract class Factory<I, O> implements Function<I, O> {
    private final FactoryResource resource;

    public Factory(@Nonnull File source, @Nonnull Class<? extends FactoryResource> clazz) {
        resource = FactoryResource.load(source, clazz);
    }

    public static class UnitFactory extends Factory<Player, Unit> {
        public UnitFactory(@Nonnull File source) {
            super(source, UnitResource.class);
        }

        @Override
        public Unit apply(Player t) {
            UnitResource resource = (UnitResource) getResource();
            Unit unit = new Unit(t, resource.getLayout().clone(), resource.getMass()) {
                @Override
                public String getType() {
                    return resource.getModel();
                }
            };
            for (Modul modul : resource.getModuls())
                modul.clone().bindUnit(unit);
            unit.setHitPoints(resource.getHitPoints());
            unit.setArmor(resource.getArmor());
            unit.setShields(resource.getShields());
            return unit;
        }
    }

    public static class ProjectileFactory extends Factory<Unit, Projectile> {
        public ProjectileFactory(@Nonnull File source) {
            super(source, ProjectileResource.class);
        }

        @Override
        public Projectile apply(Unit t) {
            ProjectileResource resource = (ProjectileResource) getResource();
            Projectile projectile = new Projectile(t.getPlayer().getParty(), resource.getLayout().clone(),
                    resource.getMass());
            projectile.setPosition(t.getPosition());
            projectile.setSpeed(resource.getSpeed());
            projectile.setRange(resource.getRange());
            projectile.setDamage(resource.getDamage());
            projectile.setCriticalChance(resource.getCriticalChance());
            projectile.setCriticalDamage(resource.getCriticalDamage());
            return projectile;
        }
    }

    public FactoryResource getResource() {
        return resource;
    }
}
