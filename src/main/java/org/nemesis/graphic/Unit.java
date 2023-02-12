package org.nemesis.graphic;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.karlz.exchange.Reference;
import com.karlz.grpc.entity.Kinetic;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

public class Unit extends Pane implements Reference<com.karlz.grpc.game.Unit> {
    private final Game game;

    public Unit(Game game) {
        this.game = game;
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
        if (kinetic == null) {
            try {
                Group model = FXMLLoader
                        .load(getClass().getClassLoader().getResource(
                                "org/nemesis/graphic/unit/" + reference.getSuper().getSuper().getType()
                                        + ".fxml"));
                getChildren().add(model);
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        kinetic = reference.getSuper();
        speed.set(reference.getSpeed());
        hitPoints.set(reference.getHitPoints());
        shields.set(reference.getShields());
        armor.set(reference.getArmor());

        setRotate(Math.toDegrees(kinetic.getRotation()) + 90);
        setLayoutX(kinetic.getPosition().getX());
        setLayoutY(kinetic.getPosition().getY());
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
