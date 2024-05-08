package org.nemesis.game;

import javafx.geometry.Point2D;

import javax.annotation.Nonnull;

public abstract class HardPoint implements Entity {
    private final @Nonnull Unit unit;

    protected HardPoint(@Nonnull Unit unit) {
        this.unit = unit;

        getUnit().getHardPoints().add(this);
    }

    @Nonnull
    public Unit getUnit() {
        return unit;
    }

    private @Nonnull Point2D position = Point2D.ZERO;

    public void setPosition(@Nonnull Point2D position) {
        this.position = position;
    }

    @Nonnull
    public Point2D getPosition() {
        return position;
    }

    @Nonnull
    public Point2D getTransformedPosition() {
        double radians = Math.toRadians(getUnit().getRotation());
        return new Point2D(getUnit().getPosition().getX() + getPosition().getX() * Math.cos(radians),
                 getUnit().getPosition().getY() + getPosition().getY() * Math.sin(radians));
    }
}
