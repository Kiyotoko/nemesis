package org.nemesis;

import java.util.HashMap;
import java.util.Map;

import org.nemesis.graphic.Region;
import org.nemesis.grpc.Creator;
import org.nemesis.grpc.NemesisClient;
import org.nemesis.grpc.NemesisServer;
import org.nemesis.grpc.StatusReply;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.ParallelCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class App extends Application {
    public final Map<String, Creator<?>> CREATORS = new HashMap<>(Map.of(
            "", () -> new Region(this)));

    int port = 8080;

    private final NemesisServer server = new NemesisServer(port);
    private final NemesisClient client = new NemesisClient(port);

    private final ObservableMap<String, Node> players = FXCollections.observableHashMap();
    private final ObservableMap<String, Region> regions = FXCollections.observableHashMap();
    private final ObservableMap<String, Node> buildings = FXCollections.observableHashMap();
    private final ObservableMap<String, Node> fleets = FXCollections.observableHashMap();

    private final BorderPane ui = new BorderPane();
    private final Group content = new Group();

    public App() {
        regions.addListener(new MapChangeListener<String, Region>() {
            @Override
            public void onChanged(Change<? extends String, ? extends Region> change) {
                if (change.wasAdded())
                    content.getChildren().add(change.getValueAdded());
                if (change.wasRemoved())
                    content.getChildren().remove(change.getValueRemoved());
            }
        });
    }

    @Override
    public void init() throws Exception {
        AnimationTimer timer = new AnimationTimer() {
            final long[] frameTimes = new long[100];
            int frameTimeIndex = 0;
            boolean arrayFilled = false;

            @Override
            public void handle(long now) {
                frameTimes[frameTimeIndex] = now;
                frameTimeIndex = (frameTimeIndex + 1) % frameTimes.length;
                if (frameTimeIndex == 0) {
                    arrayFilled = true;

                }
                if (arrayFilled) {
                    StatusReply status = client.gameStatus();
                    for (org.nemesis.grpc.Region region : status.getRegionsList()) {
                        save(regions, region.getId(), region.getType()).update(region);
                    }
                }
            }

            @SuppressWarnings("unchecked")
            private <T> T save(Map<String, T> map, String key, String type) {
                T val;
                if (!map.containsKey(key)) {
                    val = (T) CREATORS.get("").create();
                    map.put(key, val);
                } else {
                    val = map.get(key);
                }
                return val;
            }
        };
        timer.start();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(ui, 786, 786);

        SubScene subscene = new SubScene(content, 786, 786, true, SceneAntialiasing.BALANCED);
        subscene.widthProperty().bind(scene.widthProperty());
        subscene.heightProperty().bind(scene.heightProperty());

        ui.getChildren().addAll(subscene);

        Camera camera = new ParallelCamera();
        camera.setLayoutX(-786 / 2);
        camera.setLayoutY(-786 / 2);
        subscene.setCamera(camera);

        stage.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            switch (e.getCode()) {
                case W -> {
                    camera.setLayoutY(camera.getLayoutY() - 10);
                }
                case A -> {
                    camera.setLayoutX(camera.getLayoutX() - 10);
                }
                case S -> {
                    camera.setLayoutY(camera.getLayoutY() + 10);
                }
                case D -> {
                    camera.setLayoutX(camera.getLayoutX() + 10);
                }
                default -> {

                }
            }
        });

        stage.setOnCloseRequest(c -> {
            try {
                client.stop();
                server.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        stage.setScene(scene);
        stage.show();
    }

    public ObservableMap<String, Node> getPlayers() {
        return players;
    }

    public ObservableMap<String, Region> getRegions() {
        return regions;
    }

    public ObservableMap<String, Node> getBuildings() {
        return buildings;
    }

    public ObservableMap<String, Node> getFleets() {
        return fleets;
    }

    public BorderPane getUi() {
        return ui;
    }

    public Group getGameContent() {
        return content;
    }
}
