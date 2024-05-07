package org.nemesis.game;

import javafx.geometry.Point2D;
import org.nemesis.content.Identity;

import javax.annotation.Nonnull;

public class Engine extends HardPoint {
    private final @Nonnull Properties properties;

    public Engine(@Nonnull Unit unit, @Nonnull Properties properties) {
        super(unit);
        this.properties = properties;
    }

    @SuppressWarnings("unused")
    public static class Properties extends Identity {
        public Properties(@Nonnull String id) {
            super(id);
        }

        private double movementSpeed;

        public double getMovementSpeed() {
            return movementSpeed;
        }

        private double rotationSpeed;

        public double getRotationSpeed() {
            return rotationSpeed;
        }
    }

    @Override
    public void update() {
        if (!getUnit().getDestinations().isEmpty()) {
            Point2D difference = getUnit().getDestination().subtract(getUnit().getPosition());
            if (difference.magnitude() > getProperties().getMovementSpeed()) {
                double theta = Math.toDegrees(Math.atan2(difference.getX(), -difference.getY()));
                double alpha = theta - getUnit().getRotation();
                if (alpha > 180) {
                    alpha -= 360;
                }
                if (alpha < -180) {
                    alpha += 360;
                }
                if (Math.abs(alpha) > getProperties().getRotationSpeed()) {
                    getUnit().setRotation(getUnit().getRotation() + Math.signum(alpha) * getProperties().getRotationSpeed());
                } else {
                    getUnit().setRotation(theta);
                }

                double radians = Math.toRadians(getUnit().getRotation());
                Point2D next = getUnit().getPosition().subtract(-Math.sin(radians) * getProperties().getMovementSpeed(),
                        Math.cos(radians) * getProperties().getMovementSpeed());
                if (getUnit().isPositionAvailable(next)) getUnit().setPosition(next);
            } else getUnit().getDestinations().remove();
        }
    }

    @Nonnull
    public Properties getProperties() {
        return properties;
    }
}
