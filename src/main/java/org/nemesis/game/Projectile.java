package org.nemesis.game;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.karlz.bounds.Kinetic;
import com.karlz.bounds.Layout;
import com.karlz.bounds.Vector;
import com.karlz.entity.Property;

public class Projectile extends Kinetic {
	@JsonIgnore
	private transient Party party;

	public Projectile(Party party, Layout layout, double mass) {
		super(layout, Vector.ZERO, 0, 0, 0);
		party.getParent().getProjectiles().add(this);
		this.party = party;
	}

	protected void hit(Unit unit) {
		double damage = getDamage() + (int) (Math.random() + getCriticalChance()) * getCriticalDamage();
		unit.setHitPoints(unit.getHitPoints() - Math.max(damage - unit.getShields(), 0) / unit.getArmor());
		unit.setShields(Math.max(unit.getShields() - damage, 0));
	}

	@Override
	public com.karlz.grpc.game.Projectile associated() {
		return com.karlz.grpc.game.Projectile.newBuilder().setSuper((com.karlz.grpc.entity.Kinetic) super.associated())
				.setPartyId(party.getId()).setSpeed(getSpeed()).setRange(getRange()).setDamage(getDamage())
				.setCriticalChance(getCriticalChance()).setCriticalDamage(getCriticalDamage()).build();
	}

	void destroy() {

	}

	public Party getParty() {
		return party;
	}

	private Property<Double> speed;

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

	public Property<Double> rangeProperty() {
		if (range == null) {
			range = new Property<Double>(1.);
			range.addChangeListener(e -> {
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
