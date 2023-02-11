package org.nemesis.game;

import org.nemesis.game.Modul.Engine;
import org.nemesis.game.Modul.Hangar;
import org.nemesis.game.Modul.ShieldRegenerator;
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
@JsonSubTypes({ @Type(value = Weapon.class, name = "weapon"), @Type(value = Engine.class, name = "engine"),
        @Type(value = Hangar.class, name = "hangar"),
        @Type(value = ShieldRegenerator.class, name = "shieldRegenerator") })
@JsonSerialize
@JsonDeserialize
public abstract class Modul implements Children {
    public static class Weapon extends Modul {
        @JsonProperty("projectileType")
        private final String projectileType;
        @JsonProperty("fireRate")
        private final double fireRate;
        @JsonProperty("salveLength")
        private final int salveLength;

        @JsonCreator
        public Weapon(@JsonProperty("projectileType") String projectileType, @JsonProperty("fireRate") double fireRate,
                @JsonProperty("salveLength") int salveLength) {
            this.projectileType = projectileType;
            this.fireRate = fireRate;
            this.salveLength = salveLength;
        }

        @Override
        public void update(double deltaT) {

        }

        public String getProjectileType() {
            return projectileType;
        }

        public double getFireRate() {
            return fireRate;
        }

        public int getSalveLength() {
            return salveLength;
        }
    }

    public static class Engine extends Modul {
        @Override
        public void update(double deltaT) {

        }

    }

    public static class Hangar extends Modul {
        @Override
        public void update(double deltaT) {

        }

    }

    public static class ShieldRegenerator extends Modul {
        @Override
        public void update(double deltaT) {

        }

    }

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
    public Unit getParent() {
        return unit;
    }

}
