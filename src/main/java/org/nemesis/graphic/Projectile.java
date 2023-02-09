package org.nemesis.graphic;

import com.karlz.exchange.Reference;
import com.karlz.grpc.entity.Kinetic;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

public class Projectile extends Pane implements Reference<com.karlz.grpc.game.Projectile> {
    private final Game game;

    private Circle model = new Circle(); // TODO

    public Projectile(Game game) {
        this.game = game;
        getChildren().add(model);
    }

    private transient Kinetic kinetic;

    private final StringProperty partyId = new SimpleStringProperty();
    private final DoubleProperty speed = new SimpleDoubleProperty();
    private final DoubleProperty range = new SimpleDoubleProperty();
    private final DoubleProperty damage = new SimpleDoubleProperty();
    private final DoubleProperty criticalDamage = new SimpleDoubleProperty();
    private final DoubleProperty criticalChance = new SimpleDoubleProperty();

    @Override
    public void update(com.karlz.grpc.game.Projectile reference) {
        kinetic = reference.getSuper();
        partyId.set(reference.getPartyId());
        speed.set(reference.getSpeed());
        range.set(reference.getRange());
        damage.set(reference.getDamage());
        criticalDamage.set(reference.getCriticalDamage());
        criticalChance.set(reference.getCriticalChance());

        setLayoutX(kinetic.getPosition().getX());
        setLayoutY(kinetic.getPosition().getY());
        setRotate(kinetic.getRotation());
    }

    public Game getGame() {
        return game;
    }

    public Kinetic getKinetic() {
        return kinetic;
    }

    public StringProperty getPartyId() {
        return partyId;
    }

    public DoubleProperty getSpeed() {
        return speed;
    }

    public DoubleProperty getRange() {
        return range;
    }

    public DoubleProperty getDamage() {
        return damage;
    }

    public DoubleProperty getCriticalDamage() {
        return criticalDamage;
    }

    public DoubleProperty getCriticalChance() {
        return criticalChance;
    }
}
