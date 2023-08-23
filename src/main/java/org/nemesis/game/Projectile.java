package org.nemesis.game;

import io.scvis.entity.Children;
import io.scvis.entity.Kinetic;
import io.scvis.geometry.Vector2D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import javax.annotation.Nonnull;
import java.util.Random;

public class Projectile implements Kinetic, Children, Displayable {

	private final @Nonnull Player player;

	private final @Nonnull Pane pane = new Pane();

	public Projectile(@Nonnull Player player) {
		this.player = player;
		getPlayer().getGame().getProjectiles().add(this);
		getParent().getChildren().add(this);
	}

	private static final @Nonnull Random random = new Random();

	protected void hit(Unit unit) {
		unit.setHitPoints(unit.getHitPoints() - Math.max(getDamage() +
				random.nextInt((int) (1.0 + getCriticalChance())) * getCriticalDamage(), 0) / unit.getArmor());
	}

	@Override
	public void destroy() {
		Children.super.destroy();
		getPlayer().getGame().getProjectiles().remove(this);
	}

	@Nonnull
	public Player getPlayer() {
		return player;
	}

	@Nonnull
	@Override
	public Game getParent() {
		return getPlayer().getParent();
	}

	@Nonnull
    @Override
	public Node getGraphic() {
		return pane;
	}

	@Nonnull
	private Vector2D position = Vector2D.ZERO;

	public void setPosition(@Nonnull Vector2D position) {
		this.position = position;
	}

	@Nonnull
	public Vector2D getPosition() {
		return position;
	}

	private double rotation;

	public void setRotation(double rotation) {
		this.rotation = rotation;
	}

	public double getRotation() {
		return rotation;
	}

	private double speed;

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	private double range;

	public double getRange() {
		return range;
	}

	public void setRange(double range) {
		this.range = range;
	}

	private double damage;

	public double getDamage() {
		return damage;
	}

	public void setDamage(double damage) {
		this.damage = damage;
	}

	private double criticalDamage;

	public double getCriticalDamage() {
		return criticalDamage;
	}

	public void setCriticalDamage(double criticalDamage) {
		this.criticalDamage = criticalDamage;
	}

	private double criticalChance;

	public double getCriticalChance() {
		return criticalChance;
	}

	public void setCriticalChance(double criticalChance) {
		this.criticalChance = criticalChance;
	}

	@Override
	public void accelerate(double deltaT) {
		// No acceleration
	}

	@Override
	public void velocitate(double deltaT) {
		// No velocity
	}

	@Override
	public void displacement(double deltaT) {
		setPosition(getPosition().add(
				new Vector2D(Math.cos(getRotation()), Math.sin(getRotation())).multiply(getSpeed())));
		setRange(getRange() - getSpeed());
	}
}
