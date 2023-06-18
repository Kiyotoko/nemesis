package org.nemesis.game;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.scvis.entity.Children;
import io.scvis.geometry.Kinetic2D;
import io.scvis.geometry.Layout2D;
import io.scvis.geometry.Vector2D;
import io.scvis.observable.Property;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class Projectile extends Kinetic2D implements Children, Displayable {
	@JsonIgnore
	private transient Party party;

	private final Pane pane = new Pane();

	public Projectile(Party party, Layout2D layout, double mass) {
		super(layout, Vector2D.ZERO, 0, Vector2D.ZERO, Vector2D.ZERO);
		party.getParent().getProjectiles().add(this);
		getParent().getChildren().add(this);
		this.party = party;
	}

	protected void hit(Unit unit) {
		double damage = getDamage() + (int) (Math.random() + getCriticalChance()) * getCriticalDamage();
		unit.setHitPoints(unit.getHitPoints() - Math.max(damage - unit.getShields(), 0) / unit.getArmor());
		unit.setShields(Math.max(unit.getShields() - damage, 0));
	}

	public void destroy() {
		Children.super.destroy();

	}

	public Party getParty() {
		return party;
	}

	@Override
	public Node getGraphic() {
		return pane;
	}

	private Property<Double> speed;

	public Property<Double> speedProperty() {
		if (speed == null) {
			speed = new Property<Double>(1.);
		}
		return speed;
	}

	public double getSpeed() {
		return speedProperty().getValue();
	}

	public void setSpeed(double speed) {
		speedProperty().setValue(speed);
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
		return rangeProperty().getValue();
	}

	public void setRange(double range) {
		rangeProperty().setValue(range);
	}

	private Property<Double> damage;

	public Property<Double> damageProperty() {
		if (damage == null) {
			damage = new Property<Double>(1.);
		}
		return damage;
	}

	public double getDamage() {
		return damageProperty().getValue();
	}

	public void setDamage(double damage) {
		damageProperty().setValue(damage);
	}

	private Property<Double> criticalDamage;

	public Property<Double> criticalDamageProperty() {
		if (criticalDamage == null) {
			criticalDamage = new Property<Double>(2.);
		}
		return criticalDamage;
	}

	public double getCriticalDamage() {
		return criticalDamageProperty().getValue();
	}

	public void setCriticalDamage(double criticalDamage) {
		criticalDamageProperty().setValue(criticalDamage);
	}

	private Property<Double> criticalChance;

	public Property<Double> criticalChanceProperty() {
		if (criticalChance == null) {
			criticalChance = new Property<Double>(0.);
		}
		return criticalChance;
	}

	public double getCriticalChance() {
		return criticalChanceProperty().getValue();
	}

	public void setCriticalChance(double criticalChance) {
		criticalChanceProperty().setValue(criticalChance);
	}

	@Override
	public void accelerate(double deltaT) {
		// TODO Auto-generated method stub

	}

	@Override
	public void velocitate(double deltaT) {
		// TODO Auto-generated method stub

	}

	@Override
	public void displacement(double deltaT) {
		// TODO Auto-generated method stub

	}

	@Override
	public Game getParent() {
		return party.getParent();
	}
}
