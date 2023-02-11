package org.nemesis.graphic;

import com.karlz.exchange.Reference;
import com.karlz.grpc.entity.Kinetic;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

public class Unit extends Pane implements Reference<com.karlz.grpc.game.Unit> {
    private final Game game;

    private Polygon model = new Polygon(0, -20, 10, 20, -10, 20); // TODO

    public Unit(Game game) {
        this.game = game;
        model.setFill(Color.gray(.4));
        getChildren().add(model);
        addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                if (game.getSelected().contains(this))
                    return;
                if (!e.isShiftDown())
                    game.getSelected().clear();
                game.getSelected().add(this);
            }
        });
    }

    private transient Kinetic kinetic;

    private final StringProperty playerId = new SimpleStringProperty();
    private final DoubleProperty speed = new SimpleDoubleProperty();
    private final DoubleProperty hitPoints = new SimpleDoubleProperty();
    private final DoubleProperty shields = new SimpleDoubleProperty();
    private final DoubleProperty armor = new SimpleDoubleProperty();

    @Override
    public void update(com.karlz.grpc.game.Unit reference) {
        kinetic = reference.getSuper();
        speed.set(reference.getSpeed());
        hitPoints.set(reference.getHitPoints());
        shields.set(reference.getShields());
        armor.set(reference.getArmor());

        setLayoutX(kinetic.getPosition().getX());
        setLayoutY(kinetic.getPosition().getY());
        setRotate(Math.toDegrees(kinetic.getRotation()) + 90);
    }

    public Game getGame() {
        return game;
    }

    private Node icon;

    public Node getIcon() {
        if (icon == null)
            icon = new Circle(8);
        return icon;
    }

    public Kinetic getKinetic() {
        return kinetic;
    }

    public StringProperty getPlayerId() {
        return playerId;
    }

    public DoubleProperty getSpeed() {
        return speed;
    }
}
