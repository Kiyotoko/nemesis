package org.nemesis.game;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import org.nemesis.content.*;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class Unit extends GameObject {

	private final @Nonnull Deque<Point2D> destinations = new ArrayDeque<>(4);
	private final @Nonnull List<HardPoint> hardPoints = new ArrayList<>();

	private final @Nonnull Player player;
	private final @Nonnull Properties properties;

	private final @Nonnull Pane icon = new Pane();
	private final @Nonnull Polygon collision = new Polygon();

	public Unit(@Nonnull Player player, @Nonnull Properties properties) {
		super(player.getGame());
		this.player = player;
		this.properties = properties;

		setHitPoints(getProperties().getHitPoints());
		for (var entry : getProperties().getHardPointFactories().entrySet()) {
			for (var position : entry.getValue())
				entry.getKey().create(this).setPosition(position);
		}

		collision.getPoints().addAll(getProperties().getPoints());
		if (System.getProperty("ShowHitBox") != null) {
			collision.setFill(Color.TRANSPARENT);
			collision.setStroke(Color.RED);
		} else collision.setVisible(false);
		getGame().getDown().getChildren().add(getCollision());

		addEventHandler();
		if (getPlayer().isController()) {
			animation = new PathAnimation(this);
		}
		getPane().getChildren().add(new ImageView(properties.getPane().getImage()));
		getIcon().getChildren().add(new ImageView(properties.getIcon().getImage()));
		getPlayer().getUnits().add(this);
	}

	@CheckReturnValue
	@Nonnull
	public Point2D getDestination() {
		final Point2D checked = getDestinations().peek();
		return checked != null ? checked : getPosition();
	}

	public void setDestination(@Nonnull Point2D destination) {
		getDestinations().clear();
		getDestinations().add(destination);
	}

	@CheckReturnValue
	@Nonnull
	public Deque<Point2D> getDestinations() {
		return destinations;
	}

	@SuppressWarnings("all")
	public static class Properties extends Identity {

		public Properties(@Nonnull String id) {
			super(id);
		}

		private ImageBase pane;

		public ImageBase getPane() {
			return pane;
		}

		private ImageBase icon;

		public ImageBase getIcon() {
			return icon;
		}

		private Map<String, List<Point2D>> hardPoints;

		private transient Map<Factory<Unit, ? extends HardPoint>, List<Point2D>> hardPointFactories;

		public Map<Factory<Unit, ? extends HardPoint>, List<Point2D>> getHardPointFactories() {
			return hardPointFactories;
		}

		@Override
		public void withContentLoader(@Nonnull ContentLoader loader) {
			hardPointFactories = new HashMap<>();
			for (Map.Entry<String, List<Point2D>> entry : hardPoints.entrySet()) {
				getHardPointFactories().put(loader.getHardPointFactoryMap().get(entry.getKey()), entry.getValue());
			}
		}

		private List<Double> collision;

		public List<Double> getPoints() {
			return collision;
		}

		private double armor;

		public double getArmor() {
			return armor;
		}

		private double hitPoints;

		public double getHitPoints() {
			return hitPoints;
		}
	}

	@Override
	public void update() {
		for (HardPoint hardPoint : getHardPoints()) {
			hardPoint.update();
		}
	}

	/**
	 * Path animation, should be created and destroyed only if player is controller.
	 */
	private @Nullable PathAnimation animation;

	@Override
	public void destroy() {
		super.destroy();
		getPlayer().getUnits().remove(this);
		if (getPlayer().isController()) {
			getGame().getSelected().remove(this);
			if (animation != null)
				animation.destroy();
		}
		getGame().getDown().getChildren().remove(getCollision());
	}

	private void addEventHandler() {
		getPane().setPickOnBounds(true);
		getPane().addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
			if (e.getButton() == MouseButton.PRIMARY) {
				if (getPlayer().isController()) {
					if (!e.isShiftDown())
						player.getGame().getSelected().clear();
					player.getGame().getSelected().add(this);
				}
			} else if (e.getButton() == MouseButton.SECONDARY && !player.isController()) {
					getGame().getSelected().forEach(unit -> unit.setTarget(this));
					System.out.printf("Set target for %s to %s%n", getGame().getSelected(), this);
			}
		});
		getIcon().addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
			if (e.getButton() == MouseButton.PRIMARY) {
				player.getGame().getSelected().clear();
				player.getGame().getSelected().add(this);
			} else if (e.getButton() == MouseButton.SECONDARY) {
				player.getGame().getSelected().remove(this);
			}
		});
	}

	public boolean isPositionAvailable(Point2D next) {
		for (GameObject object : getGame().getObjects()) {
			if (object instanceof Unit && object != this) {
				Unit unit = (Unit) object;
				if (unit.getCollisionBounds().contains(next))
					return false;
			}
		}
		return true;
	}

	@Nonnull
	public Player getPlayer() {
		return player;
	}

	@Nonnull
	public Properties getProperties() {
		return properties;
	}

	@Nonnull
	public List<HardPoint> getHardPoints() {
		return hardPoints;
	}

	@Nonnull
	public Pane getIcon() {
		return icon;
	}

	@Nonnull
	public Node getCollision() {
		return collision;
	}

	@Nonnull
	public Bounds getCollisionBounds() {
		return getCollision().getBoundsInParent();
	}

	@Override
	public void setPosition(@Nonnull Point2D position) {
		super.setPosition(position);
		getCollision().setLayoutX(position.getX());
		getCollision().setLayoutY(position.getY());
	}

	@Override
	public void setRotation(double rotation) {
		super.setRotation(rotation);
		getCollision().setRotate(rotation);
	}

	@Nullable
	private Unit target;

	public boolean hasTarget() {
		return target != null;
	}

	@Nonnull
	public Unit getTarget() {
		if (target == null)
			return this;
		return target;
	}

	public void setTarget(@Nullable Unit target) {
		this.target = target;
	}

	public static final int UNMARKED = 0;

	private int mark = UNMARKED;

	public void setMark(int mark) {
		this.mark = mark;
	}

	public int getMark() {
		return mark;
	}
	private double hitPoints = 1;

	public double getHitPoints() {
		return hitPoints;
	}

	public void setHitPoints(double hitPoints) {
		this.hitPoints = hitPoints;
		if (hitPoints <= 0) destroy();
	}
}
