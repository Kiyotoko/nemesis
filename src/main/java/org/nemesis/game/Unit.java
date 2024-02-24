package org.nemesis.game;

import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.nemesis.content.Identity;
import org.nemesis.content.ImageBase;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.function.Function;

public class Unit extends GameObject implements Kinetic, Destroyable {

	private final @Nonnull Deque<Point2D> destinations = new ArrayDeque<>(4);

	private final @Nonnull Player player;
	private final @Nonnull Properties properties;

	public Unit(@Nonnull Player player, @Nonnull Properties properties) {
		super(player.getGame());
		this.player = player;
		this.properties = properties;

		addEventHandler();
		getPane().setVisible(getPlayer().isController());
		setDestination(position);
		if (getPlayer().isController()) {
			animation = new PathAnimation(this);
		}

		getPane().getChildren().add(new ImageView(properties.getPane().getImage()));
		getIcon().getChildren().add(new ImageView(properties.getIcon().getImage()));
		getPlayer().getUnits().add(this);
		getGame().getUnits().add(this);
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
				Point2D next = getPosition().add(difference.normalize().multiply(getProperties().getMovementSpeed()));
				if (isPositionAvailable(next)) setPosition(next);
			} else getDestinations().remove();
		}
		if (!getPlayer().isController())
			visible();
	}

	private Point2D position;

	@Override
	public void setPosition(Point2D position) {
		this.position = position;
		getPane().setLayoutX(position.getX());
		getPane().setLayoutY(position.getY());
	}

	@Override
	public Point2D getPosition() {
		return position;
	}

	public void shoot() {
		if (hasTarget() && !isReloading()) {
			if (getTarget().getHitPoints() > 0) {
				Function<Unit, Projectile> creator = getProjectileCreator();
				if (creator != null) {
					creator.apply(this);
					setReloadTime(getProperties().getReloadSpeed());
				}
			} else {
				setTarget(null);
			}
		}
		setReloadTime(Math.max(getReloadTime() - 1, 0));
	}

	public void visible() {
		Block block = getGame().getArea().getBlock(getPosition().getX(), getPosition().getY());
		if (block == null) return;
		double difference = block.getProperties().getSightDistance() * 16.0 + 2 * 12.0;
		for (Unit unit : List.copyOf(getGame().getUnits())) {
			if (unit.getPlayer() != getPlayer() && (Math.abs(unit.getPosition().getX() - getPosition().getX()) <
					difference && Math.abs(unit.getPosition().getY() - getPosition().getY()) < difference)) {
				getPane().setVisible(true);
				return;
			}
		}
		getPane().setVisible(false);
	}

	public void reveal() {
		Area area = getGame().getArea();
		Block inside = area.getBlock(getPosition().getX(), getPosition().getY());
		if (inside == null) return;
		double visibility = inside.getProperties().getSightDistance();
		for (double x = -visibility; x < visibility; x++) {
			for (double y = -visibility; y < visibility; y++) {
				Block block = area.getBlock(getPosition().getX()+x*16, getPosition().getY()+y*16);
				if (block != null) block.setHidden(true);
			}
		}
	}

	@Override
	public void destroy() {
		getPlayer().getUnits().remove(this);
		getGame().getUnits().remove(this);
		if (getPlayer().isController()) {
			getGame().getSelected().remove(this);
			if (animation != null)
				animation.destroy();
		}
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
		Block block = getGame().getArea().getBlock(next.getX(), next.getY());
		if (block != null && !block.getProperties().isBlocked()) {
			for (Unit unit : getPlayer().getGame().getUnits()) {
				if (unit != this && unit.getPosition().distance(next) < 16) return false;
			}
			return true;
		}
		return false;
	}

	@Nonnull
	public Properties getProperties() {
		return properties;
	}

	private @Nullable Function<Unit, Projectile> projectileCreator;

	public void setProjectileCreator(@Nullable Function<Unit, Projectile> projectileCreator) {
		this.projectileCreator = projectileCreator;
	}

	@Nullable
	public Function<Unit, Projectile> getProjectileCreator() {
		return projectileCreator;
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
