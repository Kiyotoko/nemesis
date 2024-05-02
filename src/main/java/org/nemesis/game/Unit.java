package org.nemesis.game;

import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.nemesis.content.Identity;
import org.nemesis.content.ImageBase;
import org.nemesis.content.ProjectileFactory;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayDeque;
import java.util.Deque;

public class Unit extends GameObject implements Kinetic {

	private final @Nonnull Deque<Point2D> destinations = new ArrayDeque<>(4);

	private final @Nonnull Player player;
	private final @Nonnull Properties properties;

	private final @Nonnull Pane icon = new Pane();

	public Unit(@Nonnull Player player, @Nonnull Properties properties) {
		super(player.getGame());
		this.player = player;
		this.properties = properties;

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

	public static class Properties extends Identity {

		public Properties(String id) {
			super(id);
		}

		private ImageBase pane;

		public Properties setPane(ImageBase pane) {
			this.pane = pane;
			return this;
		}

		public ImageBase getPane() {
			return pane;
		}

		private ImageBase icon;

		public Properties setIcon(ImageBase icon) {
			this.icon = icon;
			return this;
		}

		public ImageBase getIcon() {
			return icon;
		}

		private double armor;

		public Properties setArmor(double armor) {
			this.armor = armor;
			return this;
		}

		public double getArmor() {
			return armor;
		}

		private double reloadSpeed;

		public Properties setReloadSpeed(double reloadSpeed) {
			this.reloadSpeed = reloadSpeed;
			return this;
		}

		public double getReloadSpeed() {
			return reloadSpeed;
		}

		private double movementSpeed;

		public Properties setMovementSpeed(double movementSpeed) {
			this.movementSpeed = movementSpeed;
			return this;
		}

		public double getMovementSpeed() {
			return movementSpeed;
		}

		private double rotationSpeed;

		public double getRotationSpeed() {
			return rotationSpeed;
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

	@Nonnull
	public Pane getIcon() {
		return icon;
	}

	public void shoot() {
		if (hasTarget() && !isReloading()) {
			if (getTarget().getHitPoints() > 0) {
				ProjectileFactory creator = getProjectileFactory();
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

	@Override
	public void destroy() {
		super.destroy();
		getPlayer().getUnits().remove(this);
		if (getPlayer().isController()) {
			getGame().getSelected().remove(this);
			if (animation != null)
				animation.destroy();
		}
		getGame().getDown().getChildren().remove(getPane());
	}

	private void addEventHandler() {
		getPane().setPickOnBounds(true);
		getPane().addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
			System.out.println(e);
			if (e.getButton() == MouseButton.PRIMARY) {
				if (getPlayer().isController()) {
					if (!e.isShiftDown())
						player.getGame().getSelected().clear();
					player.getGame().getSelected().add(this);
				}
			} else if (e.getButton() == MouseButton.SECONDARY && !player.isController()) {
					getGame().getSelected().forEach(unit -> unit.setTarget(this));
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
			if (!(object instanceof Animation) && object != this && object.getPosition().distance(next) < 16)
				return false;
		}
		return true;
	}

	@Nonnull
	public Properties getProperties() {
		return properties;
	}

	private @Nullable ProjectileFactory projectileFactory;

	public void setProjectileFactory(@Nullable ProjectileFactory projectileFactory) {
		this.projectileFactory = projectileFactory;
	}

	@Nullable
	public ProjectileFactory getProjectileFactory() {
		return projectileFactory;
	}

	private @Nullable PathAnimation animation;

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
