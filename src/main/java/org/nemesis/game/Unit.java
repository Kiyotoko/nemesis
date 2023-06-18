package org.nemesis.game;

import javax.annotation.Nullable;

import io.scvis.entity.Children;
import io.scvis.geometry.Kinetic2D;
import io.scvis.geometry.Layout2D;
import io.scvis.geometry.Vector2D;
import io.scvis.observable.Property;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class Unit extends Kinetic2D implements Children, Displayable, Iconifiable {
	private final Player player;

	private final Pane pane = new Pane();

	public Unit(Player player, Layout2D layout) {
		super(layout, Vector2D.ZERO, 0, Vector2D.ZERO, Vector2D.ZERO);
		this.player = player;
		pane.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
			System.out.println(e);
			if (e.getButton() == MouseButton.PRIMARY) {
				if (!e.isShiftDown())
					player.getParty().getParent().getSelected().clear();
				if (!player.getParty().getParent().getSelected().contains(this))
					player.getParty().getParent().getSelected().add(this);
			}
		});
		player.getUnits().add(this);
		getParent().getChildren().add(this);
		getParent().getUnits().add(this);
	}

	@Override
	public void update(double deltaT) {
		super.update(deltaT);
	}

	@Override
	public void destroy() {
		Children.super.destroy();
		player.getUnits().remove(this);
		getParent().getUnits().remove(this);
	}

	public Unit place(double x, double y) {
		return place(new Vector2D(x, y));
	}

	public Unit place(Vector2D v) {
		setDestination(v);
		setPosition(v);
		return this;
	}

	@Override
	public void setRotation(double rotation) {
		pane.setRotate(rotation);
		super.setRotation(rotation);
	}

	@Override
	public void setPosition(Vector2D position) {
		pane.setLayoutX(position.getX());
		pane.setLayoutY(position.getY());
		super.setPosition(position);
	}

	@Override
	public Vector2D getDestination() {
		if (hasDestination())
			return super.getDestination();
		return getPosition();
	}

	public Player getPlayer() {
		return player;
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
		return speedProperty().getValue();
	}

	public void setSpeed(double speed) {
		speedProperty().setValue(speed);
	}

	private Property<Double> hitPoints;

	public Property<Double> hitPointsProperty() {
		if (hitPoints == null) {
			hitPoints = new Property<Double>(1.);
			hitPoints.addChangeListener(e -> {
				if (e.getNew() <= 0)
					destroy();
			});
		}
		return hitPoints;
	}

	public double getHitPoints() {
		return hitPointsProperty().getValue();
	}

	public void setHitPoints(double hitPoints) {
		hitPointsProperty().setValue(hitPoints);
	}

	private Property<Double> shields;

	public Property<Double> shieldsProperty() {
		if (shields == null) {
			shields = new Property<Double>(0.);
			shields.addInvalidationListener(e -> {
			});
		}
		return shields;
	}

	public double getShields() {
		return shieldsProperty().getValue();
	}

	public void setShields(double shields) {
		shieldsProperty().setValue(shields);
	}

	private Property<Double> armor;

	public Property<Double> armorProperty() {
		if (armor == null) {
			armor = new Property<Double>(1.);
			armor.addInvalidationListener(e -> {
			});
		}
		return armor;
	}

	public double getArmor() {
		return armorProperty().getValue();
	}

	public void setArmor(double armor) {
		armorProperty().setValue(armor);
	}

	@Override
	public Game getParent() {
		return player.getParent();
	}

	@Override
	public Pane getGraphic() {
		return pane;
	}

	private final Pane icon = new Pane();

	@Override
	public Pane getIcon() {
		return icon;
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
		double distance = getPosition().distance(getDestination());
		if (distance > 20) {
			double angle = getPosition().angle(getDestination());
			if (Math.abs(angle - getRotation()) > 0.1) {
				setRotation(getRotation() + Math.signum(angle - getRotation()) * 0.1);
			}
			setPosition(getPosition().add(new Vector2D(-Math.cos(getRotation()), -Math.sin(getRotation()))));
		}
	}
}
