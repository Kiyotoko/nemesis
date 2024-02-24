package org.nemesis.game;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;

public class ControlPoint extends GameObject implements Entity {

    private static final double CONTROL_POINT_RANGE = 40;

    private final @Nonnull Circle indic = new Circle();

    private final @Nonnull Properties properties;

    public ControlPoint(Game game, Properties properties) {
        super(game);
        this.properties = properties;

        Circle base = new Circle(CONTROL_POINT_RANGE, Color.gray(0.8, 0.5));
        base.setStrokeWidth(2);
        base.setStroke(Color.WHITE);

        getPane().getChildren().add(base);
        getPane().getChildren().add(indic);
        getPane().setLayoutX(properties.getPosition().getX());
        getPane().setLayoutY(properties.getPosition().getY());

        getGame().getControlPoints().add(this);
        getGame().getEntities().add(this);
    }

    public static class Properties implements Serializable {

        @SuppressWarnings("all")
        private Point2D position;

        public Properties setPosition(Point2D position) {
            this.position = position;
            return this;
        }

        public Point2D getPosition() {
            return position;
        }

        private double range;

        public Properties setRange(double range) {
            this.range = range;
            return this;
        }

        public double getRange() {
            return range;
        }

        private int capacity;

        public Properties setCapacity(int capacity) {
            this.capacity = capacity;
            return this;
        }

        public int getCapacity() {
            return capacity;
        }
    }

    private @Nullable Player controller;
    private double control = 0;
    private double controlTime = 0;

    @Override
    public void update() {
        for (Unit unit : getGame().getUnits()) {
            if (unit.getPosition().distance(properties.getPosition()) <= CONTROL_POINT_RANGE) {
                if (controller == unit.getPlayer()) {
                    control += 0.005;
                } else {
                    control -= 0.005;
                    if (control <= 0) {
                        controller = unit.getPlayer();
                        indic.setFill(Color.color(controller.getColor().getRed(), controller.getColor().getGreen(),
                                controller.getColor().getBlue(), 0.5));
                        control = -control;
                        controlTime = 0;
                    }
                }
            }
        }
        if (control >= 1 && controller != null) {
            controlTime += 0.065;
            if (controlTime >= 20) {
                // TODO create unit
                controlTime -= 20;
            }
            control = 1;
        }
        indic.setRadius(control * CONTROL_POINT_RANGE);
    }
}
