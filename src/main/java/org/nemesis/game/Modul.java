package org.nemesis.game;

import org.nemesis.game.Factory.ProjectileFactory;
import org.nemesis.game.Factory.UnitFactory;
import org.nemesis.game.Modul.Engine;
import org.nemesis.game.Modul.Hangar;
import org.nemesis.game.Modul.ShieldGenerator;
import org.nemesis.game.Modul.Weapon;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.karlz.entity.Children;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @Type(value = Weapon.class, name = "Weapon"), @Type(value = Engine.class, name = "Engine"),
        @Type(value = Hangar.class, name = "Hangar"),
        @Type(value = ShieldGenerator.class, name = "ShieldGenerator") })
@JsonSerialize
@JsonDeserialize
public abstract class Modul implements Children, Cloneable {
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    @JsonSerialize
    @JsonDeserialize
    public static class Weapon extends Modul {
        @JsonProperty("projectileType")
        private final String projectileType;
        @JsonProperty("cooldown")
        private final double cooldown;
        @JsonProperty("fireRate")
        private final double fireRate;
        @JsonProperty("salveLength")
        private final int salveLength;

        @JsonCreator
        public Weapon(@JsonProperty("projectileType") String projectileType, @JsonProperty("cooldown") double cooldown,
                @JsonProperty("fireRate") double fireRate, @JsonProperty("salveLength") int salveLength) {
            this.projectileType = projectileType;
            this.fireRate = fireRate;
            this.salveLength = salveLength;
            this.cooldown = cooldown;
        }

        @JsonIgnore
        private transient double cooldownTime = 0;
        @JsonIgnore
        private transient double fireRateTime = 0;
        @JsonIgnore
        private transient int salveCount = 1;

        @Override
        public void update(double deltaT) {
            if (cooldownTime <= 0) {
                if (fireRateTime <= 0) {
                    getFactory().apply(getUnit()).setPosition(getUnit().getPosition());
                    if (salveCount < salveLength) {
                        fireRateTime = fireRate;
                        salveCount++;
                    } else if (salveCount == salveLength) {
                        cooldownTime = cooldown;
                        salveCount = 1;
                    }
                } else {
                    fireRateTime -= deltaT;
                }
            } else {
                cooldownTime -= deltaT;
            }
        }

        @Override
        public Weapon clone() {
            return new Weapon(projectileType, cooldown, fireRate, salveLength);
        }

        @JsonIgnore
        private transient ProjectileFactory factory;

        public ProjectileFactory getFactory() {
            if (factory == null)
                factory = ResourceLoader.DEFAULT_RESOURCE_LOADER.getProjectileFactories().get(projectileType);
            return factory;
        }

        public String getProjectileType() {
            return projectileType;
        }

        public double getCooldown() {
            return cooldown;
        }

        public double getFireRate() {
            return fireRate;
        }

        public int getSalveLength() {
            return salveLength;
        }
    }

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    @JsonSerialize
    @JsonDeserialize
    public static class Engine extends Modul {
        @JsonProperty("acceleration")
        private final double acceleration;
        @JsonProperty("power")
        private final double power;

        @JsonCreator
        public Engine(@JsonProperty("acceleration") double acceleration, @JsonProperty("power") double power) {
            this.acceleration = acceleration;
            this.power = power;
        }

        @Override
        public void update(double deltaT) {
            getUnit().setSpeed(Math.min(acceleration * deltaT + getUnit().getSpeed(), power));
        }

        @Override
        public Engine clone() {
            return new Engine(acceleration, power);
        }

        public double getAcceleration() {
            return acceleration;
        }

        public double getPower() {
            return power;
        }
    }

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    @JsonSerialize
    @JsonDeserialize
    public static class Hangar extends Modul {
        @JsonProperty("unitType")
        private final String unitType;
        @JsonProperty("hangarSize")
        private final int hangarSize;
        @JsonProperty("sendingTime")
        protected double sendingSpeed = 4;

        @JsonCreator
        public Hangar(@JsonProperty("unitType") String unitType, @JsonProperty("hangarSize") int hangarSize,
                @JsonProperty("sendingTime") double sendingSpeed) {
            this.unitType = unitType;
            this.hangarSize = hangarSize;
            this.sendingSpeed = sendingSpeed;
        }

        @JsonIgnore
        private transient double sendingTime = 0;
        @JsonIgnore
        private transient int unitCount = 0;

        @Override
        public void update(double deltaT) {
            if (sendingTime <= 0) {
                if (unitCount <= hangarSize) {
                    getFactory().apply(getUnit().getPlayer()).setPosition(getUnit().getPosition());
                    sendingTime = sendingSpeed;
                    unitCount++;
                }
            } else {
                sendingTime -= deltaT;
            }
        }

        @Override
        public Hangar clone() {
            return new Hangar(unitType, hangarSize, sendingSpeed);
        }

        @JsonIgnore
        private transient UnitFactory factory;

        public UnitFactory getFactory() {
            if (factory == null)
                factory = ResourceLoader.DEFAULT_RESOURCE_LOADER.getUnitFactories().get(unitType);
            return factory;
        }

        public String getUnitType() {
            return unitType;
        }

        public int getHangarSize() {
            return hangarSize;
        }
    }

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    @JsonSerialize
    @JsonDeserialize
    public static class ShieldGenerator extends Modul {
        @JsonProperty("buildUp")
        private final double buildUp;
        @JsonProperty("power")
        private final double power;

        @JsonCreator
        public ShieldGenerator(@JsonProperty("buildUp") double buildUp, @JsonProperty("power") double power) {
            this.buildUp = buildUp;
            this.power = power;
        }

        @Override
        public void update(double deltaT) {
            getUnit().setShields(Math.min(buildUp * deltaT + getUnit().getShields(), power));
        }

        @Override
        public ShieldGenerator clone() {
            return new ShieldGenerator(buildUp, power);
        }

        public double getBuildUp() {
            return buildUp;
        }

        public double getPower() {
            return power;
        }
    }

    @Override
    public abstract Modul clone();

    @JsonIgnore
    private transient Unit unit;

    public boolean hasUnit() {
        return unit != null;
    }

    public Unit bindUnit(Unit unit) {
        if (!hasUnit()) {
            unit.getChildren().add(this);
            this.unit = unit;
        }
        return this.unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Unit getUnit() {
        return unit;
    }

    @Override
    @JsonIgnore
    public Unit getParent() {
        return unit;
    }
}
