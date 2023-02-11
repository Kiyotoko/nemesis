package org.nemesis.graphic;

import java.util.Map;

import org.nemesis.grpc.NemesisClient;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener.Change;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class Game extends BorderPane {
    private final NemesisClient client;

    private final ObservableMap<String, Party> parties = FXCollections.observableHashMap();
    private final ObservableMap<String, Player> players = FXCollections.observableHashMap();
    private final ObservableMap<String, Unit> units = FXCollections.observableHashMap();
    private final ObservableMap<String, Projectile> projectiles = FXCollections.observableHashMap();

    private final ObservableSet<Unit> selected = FXCollections.observableSet();

    public Game(NemesisClient client) {
        this.client = client;

        Pane down = new Pane();
        units.addListener(getGraphicListener(down));
        projectiles.addListener(getGraphicListener(down));
        getChildren().add(down);

        VBox left = new VBox();
        parties.addListener(getGraphicListener(left));
        setLeft(left);

        VBox right = new VBox();
        players.addListener(getGraphicListener(right));
        setRight(right);

        HBox bottom = new HBox();
        selected.addListener((Change<? extends Unit> change) -> {
            if (change.wasAdded())
                bottom.getChildren().add(change.getElementAdded().getIcon());
            if (change.wasRemoved())
                bottom.getChildren().add(change.getElementRemoved().getIcon());
        });
        setPadding(new Insets(20));
        setBottom(bottom);

        addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            if (e.getButton() == MouseButton.SECONDARY)
                for (Unit unit : getSelected())
                    client.change(unit.getKinetic().getSuper().getId(), new Point2D(e.getSceneX(), e.getSceneY()));
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

    public NemesisClient getClient() {
        return client;
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

    public ObservableSet<Unit> getSelected() {
        return selected;
    }
}
