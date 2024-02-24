package org.nemesis.game;

import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import org.nemesis.content.Identity;
import org.nemesis.content.ImageBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class Projectile extends GameObject implements Kinetic, Destroyable {

	private final @Nonnull Player player;
	private final @Nonnull Properties properties;

	public Projectile(@Nonnull Unit unit, @Nonnull Properties properties) {
        super(unit.getGame());
        this.player = unit.getPlayer();
		this.properties = properties;
		this.position = unit.getPosition();

        if (unit.hasTarget()) setDestination(unit.getTarget().getPosition());
		else setDestination(unit.getPosition());

		getPane().getChildren().add(new ImageView(properties.getPane().getImage()));
		getIcon().getChildren().add(new ImageView(properties.getIcon().getImage()));
		setRotation(Math.atan2(getDestination().getY() - getPosition().getY(), getDestination().getX() - getPosition().getX()));
		getGame().getProjectiles().add(this);
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

		private double movementSpeed;

		public Properties setMovementSpeed(double movementSpeed) {
			this.movementSpeed = movementSpeed;
			return this;
		}

		public double getMovementSpeed() {
			return movementSpeed;
		}

		private double damage;

		public Properties setDamage(double damage) {
			this.damage = damage;
			return this;
		}

		public double getDamage() {
			return damage;
		}

		private double criticalDamage;

		public Properties setCriticalDamage(double criticalDamage) {
			this.criticalDamage = criticalDamage;
			return this;
		}

		public double getCriticalDamage() {
			return criticalDamage;
		}

		private double criticalChance;

		public Properties setCriticalChance(double criticalChance) {
			this.criticalChance = criticalChance;
			return this;
		}

		public double getCriticalChance() {
			return criticalChance;
		}

	}

	@Override
	public void update() {
		displacement();
	}

	@Override
	public void displacement() {
		setPosition(getPosition().add(Math.cos(getRotation()) * getProperties().getMovementSpeed(), Math.sin(getRotation()) *
				getProperties().getMovementSpeed()));
		setRange(getRange() - getProperties().getMovementSpeed());
		check();
	}

	private @Nonnull Point2D position;

	@Override
	public void setPosition(@Nonnull Point2D position) {
		this.position = position;
		getPane().setLayoutX(position.getX());
		getPane().setLayoutY(position.getY());
	}

	@Override
	@Nonnull
	public Point2D getPosition() {
		return position;
	}

	private @Nullable Point2D destination;

	public void setDestination(@Nullable Point2D destination) {
		this.destination = destination;
	}

	@Nonnull
	public Point2D getDestination() {
		if (destination == null) return getPosition();
		return destination;
	}

	private static final @Nonnull Random random = new Random();

	protected void check() {
		for (Unit unit : List.copyOf(getGame().getUnits())) {
			if (unit.getPlayer() != getPlayer() && (unit.getPosition().distance(getPosition()) < 10)) {
				hit(unit);
				new DamageAnimation(this);
				destroy();
			}
		}
	}

	protected void hit(@Nonnull Unit unit) {
		unit.setHitPoints(unit.getHitPoints() - Math.max(getProperties().getDamage() +
				random.nextInt((int) (1.0 + getProperties().getCriticalChance())) * getProperties()
						.getCriticalDamage(), 0) / unit.getProperties().getArmor());
	}

	@Override
	public void destroy() {
		getGame().getProjectiles().remove(this);
	}

	@Nonnull
	public Player getPlayer() {
		return player;
	}

	@Nonnull
	public Properties getProperties() {
		return properties;
	}

	private double rotation;

	public void setRotation(double rotation) {
		this.rotation = rotation;
	}

	public double getRotation() {
		return rotation;
	}

	private double range;

	public double getRange() {
		return range;
	}

	public void setRange(double range) {
		this.range = range;
		if (range <= 0) destroy();
	}
}
