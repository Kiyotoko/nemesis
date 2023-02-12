package org.nemesis.game;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

import org.nemesis.game.FactoryResource.ProjectileResource;
import org.nemesis.game.FactoryResource.UnitResource;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.karlz.bounds.Layout;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @Type(value = UnitResource.class, name = "UnitResource"),
        @Type(value = ProjectileResource.class, name = "ProjectileResource") })
@JsonSerialize
@JsonDeserialize
public interface FactoryResource {
    @CheckReturnValue
    public static <T extends FactoryResource> T load(@Nonnull File file, @Nonnull Class<T> clazz) {
        try {
            return new JsonMapper().readValue(file, clazz);
        } catch (StreamReadException e) {
            e.printStackTrace();
        } catch (DatabindException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void save(@Nonnull File file, @Nonnull FactoryResource type) {
        try {
            new JsonMapper().writeValue(file, type);
        } catch (StreamWriteException e) {
            e.printStackTrace();
        } catch (DatabindException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    @JsonSerialize
    @JsonDeserialize
    public static class UnitResource implements FactoryResource {
        @JsonProperty("layout")
        private final Layout layout;
        @JsonProperty("moduls")
        private final List<Modul> moduls;
        @JsonProperty("model")
        private final String model;
        @JsonProperty("speed")
        private final double speed;
        @JsonProperty("hitPoints")
        private final double hitPoints;
        @JsonProperty("shields")
        private final double shields;
        @JsonProperty("armor")
        private final double armor;
        @JsonProperty("mass")
        private final double mass;

        @JsonCreator
        public UnitResource(@JsonProperty("layout") Layout layout, @JsonProperty("moduls") List<Modul> moduls,
                @JsonProperty("model") String model, @JsonProperty("speed") double speed,
                @JsonProperty("hitPoints") double hitPoints, @JsonProperty("shields") double shields,
                @JsonProperty("armor") double armor, @JsonProperty("mass") double mass) {
            this.layout = layout;
            this.moduls = moduls;
            this.model = model;
            this.speed = speed;
            this.shields = shields;
            this.hitPoints = hitPoints;
            this.armor = armor;
            this.mass = mass;
        }

        public Layout getLayout() {
            return layout;
        }

        public List<Modul> getModuls() {
            return moduls;
        }

        public String getModel() {
            return model;
        }

        public double getSpeed() {
            return speed;
        }

        public double getHitPoints() {
            return hitPoints;
        }

        public double getShields() {
            return shields;
        }

        public double getArmor() {
            return armor;
        }

        public double getMass() {
            return mass;
        }
    }

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    @JsonSerialize
    @JsonDeserialize
    public static class ProjectileResource implements FactoryResource {
        @JsonProperty("layout")
        private final Layout layout;
        @JsonProperty("model")
        private final String model;
        @JsonProperty("speed")
        private final double speed;
        @JsonProperty("range")
        private final double range;
        @JsonProperty("damage")
        private final double damage;
        @JsonProperty("criticalDamage")
        private final double criticalDamage;
        @JsonProperty("criticalChance")
        private final double criticalChance;
        @JsonProperty("mass")
        private final double mass;

        public ProjectileResource(@JsonProperty("layout") Layout layout, @JsonProperty("model") String model,
                @JsonProperty("speed") double speed, @JsonProperty("range") double range,
                @JsonProperty("damage") double damage, @JsonProperty("criticalDamage") double criticalDamage,
                @JsonProperty("criticalChance") double criticalChance, @JsonProperty("mass") double mass) {
            this.layout = layout;
            this.model = model;
            this.speed = speed;
            this.range = range;
            this.damage = damage;
            this.criticalDamage = criticalDamage;
            this.criticalChance = criticalChance;
            this.mass = mass;
        }

        public Layout getLayout() {
            return layout;
        }

        public String getModel() {
            return model;
        }

        public double getSpeed() {
            return speed;
        }

        public double getRange() {
            return range;
        }

        public double getDamage() {
            return damage;
        }

        public double getCriticalDamage() {
            return criticalDamage;
        }

        public double getCriticalChance() {
            return criticalChance;
        }

        public double getMass() {
            return mass;
        }
    }
}
