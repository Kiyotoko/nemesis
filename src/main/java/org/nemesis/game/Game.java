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
import org.nemesis.content.FileUtils;

import javax.annotation.Nonnull;
import java.util.*;

import static javafx.animation.Animation.INDEFINITE;

public class Game extends Scene implements Entity {

	// Lists for storing game objects
	private final @Nonnull List<Entity> entities = new ArrayList<>();
	private final @Nonnull ObservableList<Player> players = FXCollections.observableArrayList();
	private final @Nonnull ObservableList<Unit> units = FXCollections.observableArrayList();
	private final @Nonnull ObservableList<Projectile> projectiles = FXCollections.observableArrayList();
	private final @Nonnull ObservableList<ControlPoint> controlPoints = FXCollections.observableArrayList();
	private final @Nonnull ObservableList<Animation> animations = FXCollections.observableArrayList();
	private final @Nonnull ObservableSet<Unit> selected = FXCollections.observableSet();

	// Graphic containers
	private final @Nonnull VBox top = new VBox(2);
	private final @Nonnull Pane down = new Pane();
	private final @Nonnull HBox bottom = new HBox(8);

	// Level data object
	private @Nonnull Area area;

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
		units.addListener(getGraphicListener(down));
		projectiles.addListener(getGraphicListener(down));
		controlPoints.addListener(getGraphicListener(down));
		animations.addListener(getGraphicListener(down));
		players.addListener(getGraphicListener(top));
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
				new KeyFrame(Duration.millis(50.0), e -> update()));
		timeline.setCycleCount(INDEFINITE);
		timeline.play();
	}

	@Override
	public void update() {
		Player controller = Player.getController();
		Set<Block> oldRender = controller != null ? controller.getRenderFields() : null;
		for (Entity entity : List.copyOf(entities)) entity.update();
		if (controller != null) {
			Set<Block> newRender = controller.getRenderFields();
			Set<Block> intersection = new HashSet<>(oldRender);
			intersection.removeAll(newRender);
			for (Block block : intersection) {
				block.setHidden(false);
			}
			newRender.removeAll(oldRender);
			for (Block block : newRender) {
				block.setHidden(true);
			}
		}
	}

	protected void addEntities() {
		for (int i = 1; i < 3; i++)
			new ControlPoint(this, new ControlPoint.Properties());

		Player player = new Player(this);
		player.markAsController();
		player.setName("Player");
		player.setColor(Color.LIGHTBLUE);
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
			switch (e.getCode()) {
				case UP:
					camera.setLayoutY(camera.getLayoutY() - 10);
					break;
				case DOWN:
					camera.setLayoutY(camera.getLayoutY() + 10);
					break;
				case LEFT:
					camera.setLayoutX(camera.getLayoutX() - 10);
					break;
				case RIGHT:
					camera.setLayoutX(camera.getLayoutX() + 10);
					break;
				default: break;
			}
		});
		setOnKeyPressed(Player.getKeyEventHandler());
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

	private ListChangeListener<GameObject> getGraphicListener(Pane parent) {
		return change -> {
			change.next();
			if (change.wasAdded())
				for (int index = 0; index < change.getAddedSize(); index++) {
					GameObject object = change.getAddedSubList().get(index);
					parent.getChildren().add(object.getPane());
				}
			if (change.wasRemoved())
				for (int index = 0; index < change.getRemovedSize(); index++)
					parent.getChildren().remove(change.getRemoved().get(index).getPane());
		};
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
	public List<Unit> getUnits() {
		return units;
	}

	@Nonnull
	public List<Projectile> getProjectiles() {
		return projectiles;
	}

	@Nonnull
	public ObservableList<ControlPoint> getControlPoints() {
		return controlPoints;
	}

	@Nonnull
	public ObservableList<Animation> getAnimations() {
		return animations;
	}

	@Nonnull
	public List<Entity> getEntities() {
		return entities;
	}

	@Nonnull
	public Area getArea() {
		return area;
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
