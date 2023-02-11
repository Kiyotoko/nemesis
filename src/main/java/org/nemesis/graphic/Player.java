package org.nemesis.graphic;

import com.karlz.exchange.Reference;
import com.karlz.grpc.entity.Observable;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Player extends Pane implements Reference<com.karlz.grpc.game.Player> {
    private final Game game;

    private Label model = new Label(toString(), new Circle(5, Color.AQUAMARINE)); // TODO

    public Player(Game game) {
        this.game = game;
        model.setTextFill(Color.WHITE);
        webColor.addListener((observable, o, n) -> model.setGraphic(new Circle(5, Color.web(n))));
        userName.addListener((observable, o, n) -> model.setText(n));
        getChildren().add(model);
    }

    private Observable observable;

    private final ObservableList<String> unitIds = FXCollections.observableArrayList();
    private final StringProperty webColor = new SimpleStringProperty("");
    private final StringProperty userName = new SimpleStringProperty("");

    @Override
    public void update(com.karlz.grpc.game.Player reference) {
        if (observable == null)
            observable = reference.getSuper();
        unitIds.setAll(reference.getUnitIdsList());
        webColor.set(reference.getWebColor());
        userName.set(reference.getUserName());
    }

    public Game getGame() {
        return game;
    }

    public ObservableList<String> getUnitIds() {
        return unitIds;
    }
}
