package org.nemesis.game;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.nemesis.content.BaseUnit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ControlPoint implements Entity, Destroyable, Displayable {

    private static final double CONTROL_POINT_RANGE = 40;

    private final @Nonnull Group group = new Group();
    private final @Nonnull Circle indic = new Circle();
    private final @Nonnull Game game;
    private final @Nonnull Point2D position;

    private @Nullable Player controller;
    private double control = 0;
    private double controlTime = 0;

    public ControlPoint(@Nonnull Game game, @Nonnull Point2D position) {
        this.game = game;
        this.position = position;
        Circle base = new Circle(CONTROL_POINT_RANGE, Color.gray(0.8, 0.5));
        base.setStrokeWidth(2);
        base.setStroke(Color.WHITE);
        group.getChildren().add(base);
        group.getChildren().add(indic);
        group.setLayoutX(position.getX());
        group.setLayoutY(position.getY());
        game.getControlPoints().add(this);
        game.getEntities().add(this);
    }

    @Override
    public void update() {
        for (Unit unit : game.getUnits()) {
            if (unit.getPosition().distance(position) <= CONTROL_POINT_RANGE) {
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
                new BaseUnit(controller, position);
                controlTime -= 20;
            }
            control = 1;
        }
        indic.setRadius(control * CONTROL_POINT_RANGE);
    }

    @Override
    public void destroy() {
        game.getControlPoints().remove(this);
        game.getEntities().remove(this);
    }

    @Nonnull
    @Override
    public Node getGraphic() {
        return group;
    }
}
