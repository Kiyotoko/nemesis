package org.nemesis.game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.*;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.nemesis.content.ContentLoader;
import org.nemesis.content.Factory;

import javax.annotation.Nonnull;
import java.util.*;

import static javafx.animation.Animation.INDEFINITE;

public class Game extends Scene {

	// Lists for storing game objects
	private final @Nonnull List<Player> players = new ArrayList<>();
	private final @Nonnull List<GameObject> objects = new ArrayList<>();
	private final @Nonnull ObservableSet<Unit> selected = FXCollections.observableSet();

	// Graphic containers
	private final @Nonnull VBox top = new VBox(2);
	private final @Nonnull Pane down = new Pane();
	private final @Nonnull HBox bottom = new HBox(8);

	// Scene management
	private final @Nonnull Camera camera = new ParallelCamera();
	private double startX;
	private double startY;
	private boolean dragged = false;
	private boolean selecting = false;

	public Game(@Nonnull BorderPane pane) {
		super(pane, 800, 720, true, SceneAntialiasing.BALANCED);

		// Add content to pane
		SubScene subScene = new SubScene(getDown(), 600, 600, true, SceneAntialiasing.BALANCED);
		subScene.widthProperty().bind(widthProperty());
		subScene.heightProperty().bind(heightProperty());
		subScene.setCamera(camera);
		pane.getChildren().add(subScene);
		pane.setTop(getTop());
		pane.setBottom(getBottom());

		// Add storage listeners
		selected.addListener((SetChangeListener.Change<? extends Unit> change) -> {
			if (change.wasAdded()) {
				bottom.getChildren().add(change.getElementAdded().getIcon());
			}
			if (change.wasRemoved()) {
				bottom.getChildren().remove(change.getElementRemoved().getIcon());
			}
		});

		// Add event handlers
		addMouseClicking();
		addMouseDragging();
		addKeyHandler();

		// Set graphic settings
		top.setPadding(new Insets(10));
		bottom.setPadding(new Insets(10));
		down.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
		pane.setBackground(new Background(new BackgroundFill(Color.gray(.25), null, null)));

		// Create timeline and play it
		Timeline timeline = new Timeline(
				new KeyFrame(Duration.millis(5.0), e -> tick()));
		timeline.setCycleCount(INDEFINITE);
		timeline.play();

		Player player = new Player(this);
		player.markAsController();
		Player opponent = new Player(this);

		ContentLoader loader = new ContentLoader();
		Factory<Player, Unit> factory = loader.getUnitFactory("Unit");
		if (factory != null) {
			factory.create(player).setPosition(new Point2D(100, 400));
			factory.create(opponent).setPosition(new Point2D(400, 100));
		}
	}

	public void tick() {
		for (GameObject object : List.copyOf(getObjects())) object.update();
	}

	private void addMouseDragging() {
		Rectangle selection = new Rectangle();
		selection.setFill(Color.color(1, 1, 1, 0.125));
		selection.setStroke(Color.WHITE);
		selection.setVisible(false);
		down.getChildren().add(selection);
		addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
			if (e.getButton() == MouseButton.MIDDLE) {
				if (dragged) {
					int newX = (int) (camera.getLayoutX() - e.getSceneX() + startX);
					int newY = (int) (camera.getLayoutY() - e.getSceneY() + startY);
					camera.setLayoutX(newX);
					camera.setLayoutY(newY);
				} else {
					dragged = true;
					setCursor(Cursor.MOVE);
				}
				startX = e.getSceneX();
				startY = e.getSceneY();
			} else if (e.getButton() == MouseButton.PRIMARY) {
				if (selecting) {
					double width = camera.getLayoutX() + e.getSceneX() - startX;
					double height = camera.getLayoutY() + e.getSceneY() - startY;
					if (width < 0) {
						selection.setWidth(-width);
						selection.setX(startX + width);
					} else {
						selection.setWidth(width);
						selection.setX(startX);
					}
					if (height < 0) {
						selection.setHeight(-height);
						selection.setY(startY + height);
					} else {
						selection.setHeight(height);
						selection.setY(startY);
					}
				} else {
					selecting = true;
					startX = camera.getLayoutX() + e.getSceneX();
					startY = camera.getLayoutY() + e.getSceneY();
					selection.setX(startX);
					selection.setY(startY);
					selection.setVisible(true);
				}
			}
		});
		addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
			if (dragged) {
				dragged = false;
				setCursor(Cursor.DEFAULT);
			}
			if (selecting) {
				selecting = false;
				selection.setVisible(false);
				getSelected().clear();
				if (Player.getController() != null) {
					double minX = selection.getX();
					double minY = selection.getY();
					double maxX = minX + selection.getWidth();
					double maxY = minY + selection.getHeight();
					for (Unit unit : List.copyOf(Player.getController().getUnits())) {
						Point2D pos = unit.getPosition();
						if (minX < pos.getX() && pos.getX() < maxX && (minY < pos.getY() && pos.getY() < maxY)) {
							getSelected().add(unit);
						}
					}
				}
			}
		});
	}

	private void addKeyHandler() {
		addEventHandler(KeyEvent.KEY_PRESSED, e -> {
			if (e.getCode().isDigitKey()) {
				int number = Integer.parseInt(e.getCharacter());
				if (e.isControlDown()) {
					for (Unit unit : getSelected()) {
						unit.setMark(number);
					}
				} else {
					getSelected().clear();
					for (GameObject object : getObjects()) {
						if (object instanceof Unit) {
							Unit unit = (Unit) object;
							if (unit.getMark() == number) {
								getSelected().add(unit);
							}
						}
					}
				}
			} else {
				switch (e.getCode()) {
					case UP:
					case W:
						camera.setLayoutY(camera.getLayoutY() - 10);
						break;
					case DOWN:
						camera.setLayoutY(camera.getLayoutY() + 10);
						break;
					case LEFT:
						camera.setLayoutX(camera.getLayoutX() - 10);
						break;
					case RIGHT:
					case D:
						camera.setLayoutX(camera.getLayoutX() + 10);
						break;
					case A:
						if (e.isControlDown()) {
                            if (Player.getController() != null) {
								for (Unit unit : Player.getController().getUnits()) {
									getSelected().add(unit);
								}
							}
						} else
							camera.setLayoutX(camera.getLayoutX() - 10);
						break;
					case S:
						if (e.isControlDown()) {
							for (Unit unit : getSelected()) {
								unit.getDestinations().clear();
							}
						} else camera.setLayoutY(camera.getLayoutY() + 10);
						break;
					default:
						break;
				}
			}
		});
	}

	private void addMouseClicking() {
		addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
			if (e.getButton() == MouseButton.SECONDARY) {
				Point2D destination = new Point2D(e.getSceneX() + camera.getLayoutX(),
						e.getSceneY() + camera.getLayoutY());
				for (Unit unit : getSelected()) {
					if (e.isShiftDown()) {
						unit.getDestinations().add(destination);
					} else unit.setDestination(destination);
				}
			}
		});
	}

	@Nonnull
	public ObservableSet<Unit> getSelected() {
		return selected;
	}

	@Nonnull
	public List<Player> getPlayers() {
		return players;
	}

	@Nonnull
	public List<GameObject> getObjects() {
		return objects;
	}

	@Nonnull
	public VBox getTop() {
		return top;
	}

	@Nonnull
	public Pane getDown() {
		return down;
	}

	@Nonnull
	public HBox getBottom() {
		return bottom;
	}
}
