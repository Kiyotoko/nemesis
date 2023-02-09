package org.nemesis.game;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.karlz.bounds.Layout;
import com.karlz.bounds.Vector;
import com.karlz.entity.Kinetic;
import com.karlz.exchange.Property;

public class Projectile extends Kinetic {
	@JsonIgnore
	private transient Party party;

	public Projectile(Party party, Layout layout, double mass) {
		super(party.getParent(), layout, mass);
		party.getParent().getProjectiles().add(this);
		this.party = party;
	}

	@Override
	public void changed() {

	}

	@Override
	public Vector getAcceleration() {
		return super.getAcceleration().multiply(getSpeed());
	}

	public Party getParty() {
		return party;
	}

	private Property<Double> speed;

	@Nonnull
	public Property<Double> speedProperty() {
		if (speed == null) {
			speed = new Property<Double>(1.);
		}
		return speed;
	}

	public double getSpeed() {
		return speedProperty().get();
	}

	public void setSpeed(double speed) {
		speedProperty().set(speed);
	}

	private Property<Double> range;

	@Nonnull
	public Property<Double> rangeProperty() {
		if (range == null) {
			range = new Property<Double>(1.);
			range.addInvalidationListener(e -> {
				if (e.getNew() <= 0)
					destroy();
			});
		}
		return range;
	}

	public double getRange() {
		return rangeProperty().get();
	}

	public void setRange(double range) {
		rangeProperty().set(range);
	}

	private Property<Double> damage;

	@Nonnull
	public Property<Double> damageProperty() {
		if (damage == null) {
			damage = new Property<Double>(1.);
		}
		return damage;
	}

	public double getDamage() {
		return damageProperty().get();
	}

	public void setDamage(double damage) {
		damageProperty().set(damage);
	}

	private Property<Double> criticalDamage;

	@Nonnull
	public Property<Double> criticalDamageProperty() {
		if (criticalDamage == null) {
			criticalDamage = new Property<Double>(2.);
		}
		return criticalDamage;
	}

	public double getCriticalDamage() {
		return criticalDamageProperty().get();
	}

	public void setCriticalDamage(double criticalDamage) {
		criticalDamageProperty().set(criticalDamage);
	}

	private Property<Double> criticalChance;

	@Nonnull
	public Property<Double> criticalChanceProperty() {
		if (criticalChance == null) {
			criticalChance = new Property<Double>(0.);
		}
		return criticalChance;
	}

	public double getCriticalChance() {
		return criticalChanceProperty().get();
	}

	public void setCriticalChance(double criticalChance) {
		criticalChanceProperty().set(criticalChance);
	}
}
