package org.nemesis.game;

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
import javafx.scene.Camera;
import javafx.scene.Cursor;
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
import javafx.util.Duration;

public class Game extends Scene implements Parent {

	private final List<Children> entities = new ArrayList<>();

	private final ObservableList<Party> parties = FXCollections.observableArrayList();
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

	private final Camera camera = new ParallelCamera();
	private double startX, startY;
	private boolean dragged = false;

	public Game(BorderPane pane) {
		super(pane, 600, 600, true, SceneAntialiasing.BALANCED);

		Pane down = (Pane) subScene.getRoot();
		down.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
		units.addListener(getGraphicListener(down));
		projectiles.addListener(getGraphicListener(down));

		for (int x = 0; x <= 1000; x += 100) {
			Line line = new Line(x, 0, x, 1000);
			line.setStroke(Color.CORNFLOWERBLUE);
			down.getChildren().add(line);
		}
		for (int y = 0; y <= 1000; y += 100) {
			Line line = new Line(0, y, 1000, y);
			line.setStroke(Color.CORNFLOWERBLUE);
			down.getChildren().add(line);
		}
		pane.getChildren().add(subScene);

		VBox left = new VBox(2);
		parties.addListener(getGraphicListener(left));
		pane.setLeft(left);

		VBox right = new VBox(2);
		players.addListener(getGraphicListener(right));
		pane.setRight(right);

		HBox bottom = new HBox(2);
		selected.addListener((SetChangeListener.Change<? extends Unit> change) -> {
			if (change.wasAdded())
				bottom.getChildren().add(change.getElementAdded().getIcon());
			if (change.wasRemoved())
				bottom.getChildren().remove(change.getElementRemoved().getIcon());
			System.out.println(change);
		});
		pane.setBottom(bottom);

		addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
			if (e.getButton() == MouseButton.SECONDARY) {
				for (Unit unit : getSelected()) {
					System.out.println(e.getSceneX());
					System.out.println(e.getSceneY());
					System.out.println(camera.getLayoutX());
					System.out.println(camera.getLayoutY());
					unit.setDestination(
							new Vector2D(e.getSceneX() + camera.getLayoutX(), e.getSceneY() + camera.getLayoutX()));
				}
				System.out.println(selected);
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
					parent.getChildren().add((Node) change.getAddedSubList().get(index).getGraphic());
			if (change.wasRemoved())
				for (int index = 0; index < change.getAddedSize(); index++)
					parent.getChildren().remove((Node) change.getRemoved().get(index).getGraphic());
			System.out.println(change);
		};
	}

	public ObservableSet<Unit> getSelected() {
		return selected;
	}

	public List<Party> getParties() {
		return parties;
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

	@Override
	public List<Children> getChildren() {
		return entities;
	}

}
