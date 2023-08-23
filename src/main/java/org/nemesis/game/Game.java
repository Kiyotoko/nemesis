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
import javafx.application.Platform;
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

	private final @Nonnull List<Children> entities = new ArrayList<>();

	private final @Nonnull ObservableList<Player> players = FXCollections.observableArrayList();
	private final @Nonnull ObservableList<Unit> units = FXCollections.observableArrayList();
	private final @Nonnull ObservableList<Projectile> projectiles = FXCollections.observableArrayList();

	private final @Nonnull ObservableSet<Unit> selected = FXCollections.observableSet();

	private final @Nonnull Timeline timeline = new Timeline(
			new KeyFrame(Duration.millis(50.0), e -> update(1.0)));

	private final @Nonnull Level level = new LevelLoader(new File("src/main/resources/text/level/level.json"))
			.getLoaded();

	private final @Nonnull Camera camera = new ParallelCamera();
	private double startX;
	private double startY;
	private boolean dragged = false;

	public Game(BorderPane pane) {
		super(pane, 600, 600, true, SceneAntialiasing.BALANCED);

		Pane down = new Pane();
		down.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
		units.addListener(getGraphicListener(down));
		projectiles.addListener(getGraphicListener(down));
		down.getChildren().add(level.getGraphic());

		SubScene subScene = new SubScene(down, 600, 600, true, SceneAntialiasing.BALANCED);
		pane.getChildren().add(subScene);

		VBox top = new VBox(2);
		top.setPadding(new Insets(10));
		players.addListener(getGraphicListener(top));
		pane.setTop(top);

		HBox bottom = new HBox(8);
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

		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
	}

	private ListChangeListener<Displayable> getGraphicListener(Pane parent) {
		return change -> {
			change.next();
			if (change.wasAdded())
				for (int index = 0; index < change.getAddedSize(); index++) {
					Displayable displayable = change.getAddedSubList().get(index);
					parent.getChildren().add(displayable.getGraphic());
					if (displayable instanceof Physical)
						Platform.runLater(((Physical) displayable)::relocate);
				}
			if (change.wasRemoved())
				for (int index = 0; index < change.getRemovedSize(); index++)
					parent.getChildren().remove(change.getRemoved().get(index).getGraphic());
		};
	}

	@Nonnull
	public Timeline getTimeline() {
		return timeline;
	}

	@Nonnull
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

	@Nonnull
	public Level getLevel() {
		return level;
	}
}
