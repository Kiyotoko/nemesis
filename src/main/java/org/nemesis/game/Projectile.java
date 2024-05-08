package org.nemesis.game;

import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import org.nemesis.content.Identity;
import org.nemesis.content.ImageBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class Projectile extends GameObject {

	private final @Nonnull Unit unit;
	private final @Nonnull Properties properties;

	public Projectile(@Nonnull Weapon weapon, @Nonnull Properties properties) {
        super(weapon.getUnit().getGame());
        this.unit = weapon.getUnit();
		this.properties = properties;

        if (weapon.getUnit().hasTarget()) setDestination(weapon.getUnit().getTarget().getPosition());
		else setDestination(weapon.getUnit().getPosition());
		setRotation(weapon.getTransformedRotation());
		setPosition(weapon.getTransformedPosition());
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

		private boolean followingTarget;

		public boolean isFollowingTarget() {
			return followingTarget;
		}

		private double movementSpeed;

		public double getMovementSpeed() {
			return movementSpeed;
		}

		private double rotationSpeed;

		public double getRotationSpeed() {
			return rotationSpeed;
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
		if (getProperties().isFollowingTarget())
			follow();
		if (getProperties().getRotationSpeed() > 0)
			rotate();
		displacement();
		check();
	}

	public void follow() {
		if (getUnit().hasTarget())
			setDestination(getUnit().getTarget().getPosition());
	}

	public void rotate() {
		Point2D difference = getDestination().subtract(getPosition());
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
			setRotation(theta);
		}
	}

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
				Unit damageable = (Unit) object;
				if (damageable.getPlayer() != getUnit().getPlayer() && damageable.getCollisionBounds().intersects(
						getPane().getBoundsInParent())) {
					hit(damageable);
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
	public Unit getUnit() {
		return unit;
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
