package org.nemesis.graphic;

import java.util.Map;

import org.nemesis.grpc.NemesisClient;

import com.karlz.exchange.Mirror;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener.Change;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Camera;
import javafx.scene.Node;
import javafx.scene.ParallelCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class Game extends Scene {
	private final NemesisClient client;

	private final ObservableMap<String, Party> parties = FXCollections.observableHashMap();
	private final ObservableMap<String, Player> players = FXCollections.observableHashMap();
	private final ObservableMap<String, Unit> units = FXCollections.observableHashMap();
	private final ObservableMap<String, Projectile> projectiles = FXCollections.observableHashMap();

	private final ObservableSet<Unit> selected = FXCollections.observableSet();

	private final SubScene subScene = new SubScene(new Pane(), 600, 600, true, SceneAntialiasing.BALANCED);

	private final Camera camera = new ParallelCamera();
	private double startX, startY;
	private boolean dragged = false;

	public Game(NemesisClient client, BorderPane pane) {
		super(pane, 600, 600, true, SceneAntialiasing.BALANCED);
		this.client = client;

		Pane down = (Pane) subScene.getRoot();
		down.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
		units.addListener(getGraphicListener(down));
		projectiles.addListener(getGraphicListener(down));

		for (int x = 0; x < 600; x += 100)
			down.getChildren().add(new Line(x, 0, x, 1000));
		for (int y = 0; y < 600; y += 100)
			down.getChildren().add(new Line(0, y, 1000, y));
		pane.getChildren().add(subScene);

		VBox left = new VBox(2);
		parties.addListener(getGraphicListener(left));
		pane.setLeft(left);

		VBox right = new VBox(2);
		players.addListener(getGraphicListener(right));
		pane.setRight(right);

		HBox bottom = new HBox(2);
		selected.addListener((Change<? extends Unit> change) -> {
			if (change.wasAdded())
				bottom.getChildren().add(change.getElementAdded().getIcon());
			if (change.wasRemoved())
				bottom.getChildren().remove(change.getElementRemoved().getIcon());
		});
		pane.setPadding(new Insets(20));
		pane.setBottom(bottom);

		addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
			if (e.getButton() == MouseButton.SECONDARY)
				for (Unit unit : getSelected())
					client.change(unit.getId(),
							new Point2D(e.getSceneX() + camera.getLayoutX(), e.getSceneY() + camera.getLayoutX()));
		});
		addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
			if (e.getButton() == MouseButton.PRIMARY) {
				if (dragged) {
					int newX = (int) (camera.getLayoutX() - e.getSceneX() + startX);
					int newY = (int) (camera.getLayoutY() - e.getSceneY() + startY);
					camera.setLayoutX(newX);
					camera.setLayoutY(newY);
					startX = e.getSceneX();
					startY = e.getSceneY();
				}
				dragged = true;
				startX = e.getSceneX();
				startY = e.getSceneY();
			}
		});
		addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
			dragged = false;
		});
		pane.setBackground(new Background(new BackgroundFill(Color.gray(.1), null, null)));
		subScene.setCamera(camera);
	}

	private MapChangeListener<String, Mirror<?>> getGraphicListener(Pane parent) {
		return (change) -> {
			if (change.wasAdded())
				parent.getChildren().add((Node) change.getValueAdded().getReflection());
			if (change.wasRemoved())
				parent.getChildren().remove((Node) change.getValueRemoved().getReflection());
			System.out.println(change);
			System.out.println(change.getValueAdded().getReflection());
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
