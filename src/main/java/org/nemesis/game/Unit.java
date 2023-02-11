package org.nemesis.game;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.nemesis.grpc.NemesisServer.NemesisDispatchHelper;

import com.google.protobuf.Message;
import com.karlz.bounds.Layout;
import com.karlz.bounds.Vector;
import com.karlz.entity.Kinetic;
import com.karlz.entity.Parent;
import com.karlz.exchange.Property;

public class Unit extends Kinetic implements Parent {
    private final Player player;

    private final List<Modul> moduls = new ArrayList<>();

    public Unit(Player player, Layout layout, double mass) {
        super(player.getParty().getParent(), layout, mass);
        player.getParty().getParent().getUnits().add(this);
        player.getControllerTargets().put(getId(), this);
        player.getUnits().add(this);
        this.player = player;
    }

    @Override
    public void update(double deltaT) {
        Parent.super.update(deltaT);
        super.update(deltaT);
        changed();
    }

    protected void changed() {
        for (NemesisDispatchHelper helper : ((Game) getParent()).getDispatchers().values()) {
            helper.getUnits().add(this);
        }
    }

    @Override
    protected void accelerate(double deltaT) {
    }

    @Override
    protected void velocitate(double deltaT) {
    }

    @Override
    protected void displacement(double deltaT) {
        Vector dif = getDestination().subtract(getPosition());
        if (dif.magnitude() > 3) {
            double alpha = getRotation() + Math.signum(Math.atan2(dif.getY(), dif.getX()) - getRotation()) * deltaT;
            Vector next = getPosition().add(new Vector(Math.cos(alpha), Math.sin(alpha)));

            if (world != null) {
                if (world.isInside(next))
                    setPosition(next);
            } else
                setPosition(next);
            getLayout().setRotation(alpha);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        player.getControllerTargets().remove(getId());
        player.getChildren().remove(this);
    }

    @Override
    public Message associated() {
        return com.karlz.grpc.game.Unit.newBuilder().setSuper((com.karlz.grpc.entity.Kinetic) super.associated())
                .setSpeed(getSpeed()).setHitPoints(getHitPoints()).setArmor(getArmor()).setShields(getShields())
                .setPlayerId(getPlayer().getId()).build();
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public List<Modul> getChildren() {
        return moduls;
    }

    @Nullable
    private Unit target;

    public boolean hasTarget() {
        return target != null;
    }

    public Unit getTarget() {
        return target;
    }

    public void setTarget(Unit target) {
        this.target = target;
    }

    private Property<Double> speed;

    public Property<Double> speedProperty() {
        if (speed == null)
            speed = new Property<Double>(1.);
        return speed;
    }

    public double getSpeed() {
        return speedProperty().get();
    }

    public void setSpeed(double speed) {
        speedProperty().set(speed);
    }

    private Property<Double> hitPoints;

    public Property<Double> hitPointsProperty() {
        if (hitPoints == null) {
            hitPoints = new Property<Double>(1.);
            hitPoints.addInvalidationListener(e -> {
                if (e.getNew() <= 0)
                    destroy();
                else
                    changed();
            });
        }
        return hitPoints;
    }

    public double getHitPoints() {
        return hitPointsProperty().get();
    }

    public void setHitPoints(double hitPoints) {
        hitPointsProperty().set(hitPoints);
    }

    private Property<Double> shields;

    public Property<Double> shieldsProperty() {
        if (shields == null) {
            shields = new Property<Double>(0.);
            shields.addInvalidationListener(e -> {
                changed();
            });
        }
        return shields;
    }

    public double getShields() {
        return shieldsProperty().get();
    }

    public void setShields(double shields) {
        shieldsProperty().set(shields);
    }

    private Property<Double> armor;

    public Property<Double> armorProperty() {
        if (armor == null) {
            armor = new Property<Double>(1.);
            armor.addInvalidationListener(e -> {
                changed();
            });
        }
        return armor;
    }

    public double getArmor() {
        return armorProperty().get();
    }

    public void setArmor(double armor) {
        armorProperty().set(armor);
    }
}
