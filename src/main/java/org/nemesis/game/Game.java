package org.nemesis.game;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.scvis.entity.Children;
import io.scvis.entity.Parent;
import io.scvis.geometry.Vector2D;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.geometry.Insets;
import javafx.scene.Camera;
import javafx.scene.Cursor;
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
import javafx.util.Duration;
import org.nemesis.content.LevelLoader;

import javax.annotation.Nonnull;

public class Game extends Scene implements Parent {

	private final List<Children> entities = new ArrayList<>();

	private final ObservableList<Player> players = FXCollections.observableArrayList();
	private final ObservableList<Unit> units = FXCollections.observableArrayList();
	private final ObservableList<Projectile> projectiles = FXCollections.observableArrayList();

	private final ObservableSet<Unit> selected = FXCollections.observableSet();

	private final SubScene subScene = new SubScene(new Pane(), 600, 600, true, SceneAntialiasing.BALANCED);
	private final Timeline timeline = new Timeline(new KeyFrame(Duration.millis(50.0), e -> {
		update(1.0);
	}));
	{
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
	}

	Level level = new LevelLoader(new File("src/main/resources/text/level/level.json")).getLoaded();

	private final Camera camera = new ParallelCamera();
	private double startX;
	private double startY;
	private boolean dragged = false;

	public Game(BorderPane pane) {
		super(pane, 600, 600, true, SceneAntialiasing.BALANCED);

		Pane down = (Pane) subScene.getRoot();
		down.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
		units.addListener(getGraphicListener(down));
		projectiles.addListener(getGraphicListener(down));

		down.getChildren().add(level.getGraphic());

		pane.getChildren().add(subScene);

		VBox right = new VBox(2);
		right.setPadding(new Insets(10));
		players.addListener(getGraphicListener(right));
		pane.setRight(right);

		HBox bottom = new HBox(2);
		bottom.setPadding(new Insets(10));
		selected.addListener((SetChangeListener.Change<? extends Unit> change) -> {
			if (change.wasAdded())
				bottom.getChildren().add(change.getElementAdded().getIcon());
			if (change.wasRemoved())
				bottom.getChildren().remove(change.getElementRemoved().getIcon());
		});
		pane.setBottom(bottom);

		addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
			if (e.getButton() == MouseButton.SECONDARY) {
				for (Unit unit : getSelected()) {
					unit.setDestination(
							new Vector2D(e.getSceneX() + camera.getLayoutX(), e.getSceneY() + camera.getLayoutY()));
				}
			}
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
				setCursor(Cursor.MOVE);
				startX = e.getSceneX();
				startY = e.getSceneY();
			}
		});
		addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
			dragged = false;
			setCursor(Cursor.DEFAULT);
		});
		pane.setBackground(new Background(new BackgroundFill(Color.gray(.25), null, null)));
		subScene.widthProperty().bind(widthProperty());
		subScene.heightProperty().bind(heightProperty());
		subScene.setCamera(camera);
	}

	private ListChangeListener<Displayable> getGraphicListener(Pane parent) {
		return (change) -> {
			change.next();
			if (change.wasAdded())
				for (int index = 0; index < change.getAddedSize(); index++)
					parent.getChildren().add(change.getAddedSubList().get(index).getGraphic());
			if (change.wasRemoved())
				for (int index = 0; index < change.getAddedSize(); index++)
					parent.getChildren().remove(change.getRemoved().get(index).getGraphic());
		};
	}

	public ObservableSet<Unit> getSelected() {
		return selected;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public List<Unit> getUnits() {
		return units;
	}

	public List<Projectile> getProjectiles() {
		return projectiles;
	}

	@Nonnull
	@Override
	public List<Children> getChildren() {
		return entities;
	}

	public Level getLevel() {
		return level;
	}
}
