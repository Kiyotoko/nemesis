package org.nemesis.game;

import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import org.nemesis.content.Identity;
import org.nemesis.content.ImageBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class Projectile extends GameObject implements Kinetic {

	private final @Nonnull Player player;
	private final @Nonnull Properties properties;

	public Projectile(@Nonnull Unit unit, @Nonnull Properties properties) {
        super(unit.getGame());
        this.player = unit.getPlayer();
		this.properties = properties;

        if (unit.hasTarget()) setDestination(unit.getTarget().getPosition());
		else setDestination(unit.getPosition());
		setRotation(unit.getRotation());
		setPosition(unit.getPosition());
		setRange(getProperties().getRange());

		getPane().getChildren().add(new ImageView(properties.getPane().getImage()));
	}

	@SuppressWarnings("unused")
	public static class Properties extends Identity {
		public Properties(String id) {
			super(id);
		}

		private ImageBase pane;

		public ImageBase getPane() {
			return pane;
		}

		private double movementSpeed;

		public double getMovementSpeed() {
			return movementSpeed;
		}

		private double damage;

		public double getDamage() {
			return damage;
		}

		private double criticalDamage;

		public double getCriticalDamage() {
			return criticalDamage;
		}

		private double criticalChance;

		public double getCriticalChance() {
			return criticalChance;
		}

		private double range;

		public double getRange() {
			return range;
		}
	}

	@Override
	public void update() {
		displacement();
		check();
	}

	@Override
	public void displacement() {
		double radians = Math.toRadians(getRotation());
		Point2D next = getPosition().subtract(-Math.sin(radians) * getProperties().getMovementSpeed(),
				Math.cos(radians) * getProperties().getMovementSpeed());
		setPosition(next);
		setRange(getRange() - getProperties().getMovementSpeed());
	}

	private static final @Nonnull Random random = new Random();

	protected void check() {
		for (GameObject object : List.copyOf(getGame().getObjects())) {
			if (object instanceof Unit) {
				Unit unit = (Unit) object;
				if (unit.getPlayer() != getPlayer() && unit.getPane().getBoundsInParent().intersects(
						getPane().getBoundsInParent())) {
					hit(unit);
					new DamageAnimation(this);
					destroy();
				}
			}
		}
	}

	protected void hit(@Nonnull Unit unit) {
		double criticalDamage = random.nextInt((int) (1.0 + getProperties().getCriticalChance())) * getProperties()
				.getCriticalDamage();
		double damage = (getProperties().getDamage() + criticalDamage) * unit.getProperties().getArmor();
		unit.setHitPoints(unit.getHitPoints() - damage);
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

	@Nonnull
	public Player getPlayer() {
		return player;
	}

	@Nonnull
	public Properties getProperties() {
		return properties;
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
