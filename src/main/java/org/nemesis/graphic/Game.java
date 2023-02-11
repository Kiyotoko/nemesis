package org.nemesis.graphic;

import java.util.Map;

import org.nemesis.grpc.NemesisClient;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class Game extends BorderPane {
    private final ObservableMap<String, Party> parties = FXCollections.observableHashMap();
    private final ObservableMap<String, Player> players = FXCollections.observableHashMap();
    private final ObservableMap<String, Unit> units = FXCollections.observableHashMap();
    private final ObservableMap<String, Projectile> projectiles = FXCollections.observableHashMap();

    public Game(NemesisClient client) {
        HBox top = new HBox();
        parties.addListener(getGraphicListener(top));
        setLeft(top);
        VBox left = new VBox();
        players.addListener(getGraphicListener(left));
        setRight(left);
        units.addListener(getGraphicListener(this));
        projectiles.addListener(getGraphicListener(this));

        addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            for (Unit unit : units.values()) {
                client.change(unit.getKinetic().getSuper().getId(), new Point2D(e.getSceneX(), e.getSceneY()));
            }
        });
        setBackground(new Background(new BackgroundFill(Color.gray(.1), null, null)));
    }

    private MapChangeListener<String, Pane> getGraphicListener(Pane parent) {
        return (change) -> {
            if (change.wasAdded())
                parent.getChildren().add(change.getValueAdded());
            if (change.wasRemoved())
                parent.getChildren().add(change.getValueRemoved());
            System.out.println(change);
        };
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
