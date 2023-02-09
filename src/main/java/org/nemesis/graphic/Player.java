package org.nemesis.graphic;

import com.karlz.exchange.Reference;
import com.karlz.grpc.entity.Observable;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;

public class Player extends Pane implements Reference<com.karlz.grpc.game.Player> {
    private final Game game;

    public Player(Game game) {
        this.game = game;
    }

    private final ObservableList<String> unitIds = FXCollections.observableArrayList();

    private Observable observable;

    @Override
    public void update(com.karlz.grpc.game.Player reference) {
        if (observable == null)
            observable = reference.getSuper();
        unitIds.setAll(reference.getUnitIdsList());
    }

    public Game getGame() {
        return game;
    }

    public ObservableList<String> getUnitIds() {
        return unitIds;
    }
}
