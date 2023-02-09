package org.nemesis.graphic;

import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.scene.Group;
import javafx.scene.layout.Pane;

public class Game extends Group {
    private final ObservableMap<String, Party> parties = FXCollections.observableHashMap();
    private final ObservableMap<String, Player> players = FXCollections.observableHashMap();
    private final ObservableMap<String, Unit> units = FXCollections.observableHashMap();
    private final ObservableMap<String, Projectile> projectiles = FXCollections.observableHashMap();

    public Game() {
        final MapChangeListener<String, Pane> listener = (change) -> {
            if (change.wasAdded())
                getChildren().add(change.getValueAdded());
            if (change.wasRemoved())
                getChildren().add(change.getValueRemoved());
        };

        parties.addListener(listener);
        players.addListener(listener);
        units.addListener(listener);
        projectiles.addListener(listener);
    }

    public Map<String, Party> getParties() {
        return parties;
    }

    public Map<String, Player> getPlayers() {
        return players;
    }

    public Map<String, Unit> getUnits() {
        return units;
    }

    public Map<String, Projectile> getProjectiles() {
        return projectiles;
    }
}
