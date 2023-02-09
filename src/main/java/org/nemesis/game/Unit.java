package org.nemesis.game;

import javax.annotation.Nonnull;

import com.karlz.bounds.Layout;
import com.karlz.bounds.Vector;
import com.karlz.entity.Kinetic;
import com.karlz.exchange.Property;

public class Unit extends Kinetic {
	private final Player player;

	public Unit(Player player, Layout layout, double mass) {
		super(player.getParty().getParent(), layout, mass);
		player.getParty().getParent().getUnits().add(this);
		player.getUnits().add(this);
		this.player = player;
	}

	@Override
	public void changed() {

	}

	@Override
	public void destroy() {
		super.destroy();
		player.getChildren().remove(this);
	}

	@Override
	public Vector getAcceleration() {
		return super.getAcceleration().multiply(getSpeed());
	}

	public Player getPlayer() {
		return player;
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

	private Property<Double> hitPoints;

	@Nonnull
	public Property<Double> hitPointsProperty() {
		if (hitPoints == null) {
			hitPoints = new Property<Double>(1.);
			hitPoints.addInvalidationListener(e -> {
				if (e.getNew() <= 0)
					destroy();
			});
		}
		return hitPoints;
	}

	public double getHitPoints() {
		return hitPointsProperty().get();
	}

	public void setHitPoints(double hitPoints) {
		hitPointsProperty().set(hitPoints);
	}

	private Property<Double> shields;

	@Nonnull
	public Property<Double> shieldsProperty() {
		if (shields == null) {
			shields = new Property<Double>(0.);
		}
		return shields;
	}

	public double getShields() {
		return shieldsProperty().get();
	}

	public void setShields(double shields) {
		shieldsProperty().set(shields);
	}

	private Property<Double> armor;

	@Nonnull
	public Property<Double> armorProperty() {
		if (armor == null) {
			armor = new Property<Double>(1.);
		}
		return armor;
	}

	public double getArmor() {
		return armorProperty().get();
	}

	public void setArmor(double armor) {
		armorProperty().set(armor);
	}
}
