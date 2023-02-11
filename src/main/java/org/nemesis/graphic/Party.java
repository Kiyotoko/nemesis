package org.nemesis.graphic;

import com.karlz.exchange.Reference;
import com.karlz.grpc.entity.Observable;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Party extends Pane implements Reference<com.karlz.grpc.game.Party> {
    private final Game game;

    private Label model = new Label(toString(), new Circle(5, Color.CRIMSON)); // TODO

    public Party(Game game) {
        this.game = game;
        model.setTextFill(Color.WHITE);
        getChildren().add(model);
    }

    private transient Observable observable;

    private final ObservableList<String> playerIds = FXCollections.observableArrayList();

    @Override
    public void update(com.karlz.grpc.game.Party reference) {
        if (observable == null)
            observable = reference.getSuper();
        playerIds.setAll(reference.getPlayerIdsList());
    }

    public Game getGame() {
        return game;
    }

    public Observable getObservable() {
        return observable;
    }

    public ObservableList<String> getPlayerIds() {
        return playerIds;
    }
}
