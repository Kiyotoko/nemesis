package org.nemesis.graphic;

import com.karlz.exchange.Reference;
import com.karlz.grpc.entity.Observable;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;

public class Party extends Pane implements Reference<com.karlz.grpc.game.Party> {
    private final Game game;

    public Party(Game game) {
        this.game = game;
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
