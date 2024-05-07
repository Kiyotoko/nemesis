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

import javax.annotation.CheckForNull;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class Unit extends GameObject implements Kinetic {

	private final @Nonnull Deque<Point2D> destinations = new ArrayDeque<>(4);

	private final @Nonnull Player player;
	private final @Nonnull Properties properties;

	private final @Nonnull Pane icon = new Pane();
	private final @Nonnull Polygon collision = new Polygon();

	public Unit(@Nonnull Player player, @Nonnull Properties properties) {
		super(player.getGame());
		this.player = player;
		this.properties = properties;

		setHitPoints(getProperties().getHitPoints());
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

	@SuppressWarnings("unused")
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

		private String projectileId;

		private transient Factory<Unit, Projectile> factory;

		@Override
		public void withContentLoader(@Nonnull ContentLoader loader) {
			factory = loader.getProjectileFactory(projectileId);
		}

		@CheckForNull
		public Factory<Unit, Projectile> getFactory() {
			return factory;
		}

		private List<Double> collision;

		public List<Double> getPoints() {
			return collision;
		}

		private double armor;

		public double getArmor() {
			return armor;
		}

		private double reloadSpeed;

		public double getReloadSpeed() {
			return reloadSpeed;
		}

		private double movementSpeed;

		public double getMovementSpeed() {
			return movementSpeed;
		}

		private double rotationSpeed;

		public double getRotationSpeed() {
			return rotationSpeed;
		}

		private double hitPoints;

		public double getHitPoints() {
			return hitPoints;
		}
	}

	@Override
	public void update() {
		displacement();
		shoot();
	}

	@Override
	public void displacement() {
		if (!getDestinations().isEmpty()) {
			Point2D difference = getDestination().subtract(getPosition());
			if (difference.magnitude() > getProperties().getMovementSpeed()) {
				double theta = Math.toDegrees(Math.atan2(difference.getX(), -difference.getY()));
				double alpha = theta - getRotation();
				if (alpha > 180) {
					alpha -= 360;
				}
				if (alpha < -180) {
					alpha += 360;
				}
				if (Math.abs(alpha) > getProperties().getRotationSpeed()) {
					setRotation(getRotation() + Math.signum(alpha) * getProperties().getRotationSpeed());
				} else {
					setRotation(getRotation() + alpha);
				}

				double radians = Math.toRadians(getRotation());
				Point2D next = getPosition().subtract(-Math.sin(radians) * getProperties().getMovementSpeed(),
						Math.cos(radians) * getProperties().getMovementSpeed());
				if (isPositionAvailable(next)) setPosition(next);
			} else getDestinations().remove();
		}
	}

	public void shoot() {
		if (hasTarget() && !isReloading()) {
			if (getTarget().getHitPoints() > 0) {
				Factory<Unit, Projectile> creator = getProperties().getFactory();
				if (creator != null) {
					creator.create(this);
					setReloadTime(getProperties().getReloadSpeed());
				}
			} else {
				setTarget(null);
			}
		}
		setReloadTime(Math.max(getReloadTime() - 1, 0));
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
	public Properties getProperties() {
		return properties;
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

	private double reloadTime = 0;

	public boolean isReloading() {
		return reloadTime > 0;
	}

	public void setReloadTime(double reloadTime) {
		this.reloadTime = reloadTime;
	}

	public double getReloadTime() {
		return reloadTime;
	}

	private double hitPoints = 1;

	public double getHitPoints() {
		return hitPoints;
	}

	public void setHitPoints(double hitPoints) {
		this.hitPoints = hitPoints;
		if (hitPoints <= 0) destroy();
	}

	@Nonnull
	public Player getPlayer() {
		return player;
	}
}
