package org.nemesis.game;

import javax.annotation.Nullable;

import org.nemesis.grpc.NemesisServer.NemesisDispatchHelper;

import com.google.protobuf.Message;
import com.karlz.bounds.Kinetic;
import com.karlz.bounds.Layout;
import com.karlz.bounds.Vector;
import com.karlz.entity.Children;
import com.karlz.entity.Property;

public class Unit extends Kinetic implements Children {
	private final Player player;
	private final String model;

	public Unit(Player player, Layout layout, String model, double rotation, double acceleration, double velocity) {
		super(layout, Vector.ZERO, rotation, acceleration, velocity);
		this.player = player;
		this.model = model;
		player.getParty().getParent().getUnits().add(this);
		player.getControllerTargets().put(getId(), this);
		player.getUnits().add(this);
	}

	@Override
	public void update(double deltaT) {
		super.update(deltaT);
		changed();
	}

	protected void changed() {
		for (NemesisDispatchHelper helper : getParent().getParent().getParent().getDispatchers().values()) {
			helper.getUnits().add(this);
		}
	}

	@Override
	public void destroy() {
		player.getControllerTargets().remove(getId());
		player.getChildren().remove(this);
	}

	@Override
	public Message associated() {
		return com.karlz.grpc.game.Unit.newBuilder().setKinetic((com.karlz.grpc.entity.Kinetic) super.associated())
				.setModel(getModel()).setHitPoints(getHitPoints()).setArmor(getArmor()).setShields(getShields())
				.setPlayerId(getPlayer().getId()).build();
	}

	public Unit place(double x, double y) {
		return place(new Vector(x, y));
	}

	public Unit place(Vector v) {
		setDestination(v);
		setPosition(v);
		return this;
	}

	@Override
	public Vector getDestination() {
		if (hasDestination())
			return super.getDestination();
		return getPosition();
	}

	public Player getPlayer() {
		return player;
	}

	public String getModel() {
		return model;
	}

	@Nullable
	private Unit target;

	public boolean hasTarget() {
		return target != null;
	}

	public Unit getTarget() {
		return target;
	}

	public void setTarget(Unit target) {
		this.target = target;
	}

	private Property<Double> speed;

	public Property<Double> speedProperty() {
		if (speed == null)
			speed = new Property<Double>(1.);
		return speed;
	}

	public double getSpeed() {
		return speedProperty().get();
	}

	public void setSpeed(double speed) {
		speedProperty().set(speed);
	}

	private Property<Double> hitPoints;

	public Property<Double> hitPointsProperty() {
		if (hitPoints == null) {
			hitPoints = new Property<Double>(1.);
			hitPoints.addChangeListener(e -> {
				if (e.getNew() <= 0)
					destroy();
				else
					changed();
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

	public Property<Double> shieldsProperty() {
		if (shields == null) {
			shields = new Property<Double>(0.);
			shields.addInvalidationListener(e -> {
				changed();
			});
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

	public Property<Double> armorProperty() {
		if (armor == null) {
			armor = new Property<Double>(1.);
			armor.addInvalidationListener(e -> {
				changed();
			});
		}
		return armor;
	}

	public double getArmor() {
		return armorProperty().get();
	}

	public void setArmor(double armor) {
		armorProperty().set(armor);
	}

	@Override
	public Player getParent() {
		return player;
	}
}
