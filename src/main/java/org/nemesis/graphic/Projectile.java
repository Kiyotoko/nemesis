package org.nemesis.graphic;

import com.karlz.exchange.Mirror;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.Pane;

public class Projectile extends Mirror<com.karlz.grpc.game.Projectile> {
	private final Game game;

	public Projectile(Game game) {
		super(new Pane());
		this.game = game;
	}

	private final StringProperty partyId = new SimpleStringProperty();
	private final DoubleProperty speed = new SimpleDoubleProperty();
	private final DoubleProperty range = new SimpleDoubleProperty();
	private final DoubleProperty damage = new SimpleDoubleProperty();
	private final DoubleProperty criticalDamage = new SimpleDoubleProperty();
	private final DoubleProperty criticalChance = new SimpleDoubleProperty();

	@Override
	public void update(com.karlz.grpc.game.Projectile reference) {
		if (!hasSource())
			setSource(reference.getSuper().getIdentifiable());

		partyId.set(reference.getPartyId());
		speed.set(reference.getSpeed());
		range.set(reference.getRange());
		damage.set(reference.getDamage());
		criticalDamage.set(reference.getCriticalDamage());
		criticalChance.set(reference.getCriticalChance());

		getReflection().setLayoutX(reference.getSuper().getLayout().getPosition().getX());
		getReflection().setLayoutY(reference.getSuper().getLayout().getPosition().getY());
		getReflection().setRotate(reference.getSuper().getLayout().getRotation());
	}

	@Override
	public Pane getReflection() {
		return (Pane) super.getReflection();
	}

	public Game getGame() {
		return game;
	}

	public StringProperty getPartyId() {
		return partyId;
	}

	public DoubleProperty getSpeed() {
		return speed;
	}

	public DoubleProperty getRange() {
		return range;
	}

	public DoubleProperty getDamage() {
		return damage;
	}

	public DoubleProperty getCriticalDamage() {
		return criticalDamage;
	}

	public DoubleProperty getCriticalChance() {
		return criticalChance;
	}
}
