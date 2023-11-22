package org.nemesis.game;

import io.scvis.geometry.Vector2D;

import javax.annotation.CheckForNull;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

public class Projectile extends Physical {

	public Projectile(@Nonnull Unit unit) {
		super(unit.getPlayer(), unit.getPosition());

		if (unit.hasTarget())
			setDestination(unit.getTarget().getDestination());
		else {
			setDestination(unit.getPosition());
		}
		setRotation(getPosition().angle(getDestination()));

		getParent().getProjectiles().add(this);
		getParent().getChildren().add(this);
	}

	@Override
	public void displacement(double deltaT) {
		setPosition(getPosition().add(
				new Vector2D(-Math.cos(getRotation()), -Math.sin(getRotation())).multiply(getSpeed())));
		setRange(getRange() - getSpeed());
		check();
	}

	private static final @Nonnull Random random = new Random();

	protected void check() {
		for (Unit unit : List.copyOf(getParent().getUnits())) {
			if (unit.getPlayer() != getPlayer() && (unit.getPosition().distance(getPosition()) < 10)) {
				hit(unit);
				new DamageAnimation(this);
				destroy();
			}
		}
	}

	protected void hit(@Nonnull Unit unit) {
		unit.setHitPoints(unit.getHitPoints() - Math.max(getDamage() +
				random.nextInt((int) (1.0 + getCriticalChance())) * getCriticalDamage(), 0) / unit.getArmor());
	}

	@Override
	public void destroy() {
		super.destroy();
		getParent().getProjectiles().remove(this);
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

	private double damage;

	@CheckReturnValue
	public double getDamage() {
		return damage;
	}

	public void setDamage(double damage) {
		this.damage = damage;
	}

	private double criticalDamage;

	@CheckReturnValue
	public double getCriticalDamage() {
		return criticalDamage;
	}

	public void setCriticalDamage(double criticalDamage) {
		this.criticalDamage = criticalDamage;
	}

	private double criticalChance;

	@CheckReturnValue
	public double getCriticalChance() {
		return criticalChance;
	}

	public void setCriticalChance(double criticalChance) {
		this.criticalChance = criticalChance;
	}
}
