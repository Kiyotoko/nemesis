package org.nemesis.game;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import io.scvis.entity.Children;
import io.scvis.entity.Kinetic;
import io.scvis.geometry.Vector2D;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class Unit implements Kinetic, Children, Displayable, Iconifiable {
	private final @Nonnull Player player;

	private final @Nonnull Pane pane = new Pane();
	private final @Nonnull Pane icon = new Pane();

	public Unit(@Nonnull Player player) {
		this.player = player;
		pane.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
			if (getPlayer().isController()){
				if (e.getButton() == MouseButton.PRIMARY) {
					if (!e.isShiftDown())
						player.getGame().getSelected().clear();
					player.getGame().getSelected().add(this);
				} else if (e.getButton() == MouseButton.SECONDARY) {
					for (Unit unit: getParent().getSelected()) {
						unit.setTarget(this);
					}
				}
			}
		});
		player.getUnits().add(this);
		getParent().getChildren().add(this);
		getParent().getUnits().add(this);
	}

	@Override
	public void destroy() {
		Children.super.destroy();
		getPlayer().getUnits().remove(this);
	}

	@Nonnull
	public Player getPlayer() {
		return player;
	}

	@Nullable
	private Unit target;

	public boolean hasTarget() {
		return target != null;
	}

	@Nonnull
	public Unit getTarget() {
		if (target == null)
			return this;
		return target;
	}

	public void setTarget(@Nullable Unit target) {
		this.target = target;
	}

	@Nonnull
	private Vector2D position = Vector2D.ZERO;

	public void setPosition(@Nonnull Vector2D position) {
		this.position = position;
		pane.setLayoutX(position.getX() - pane.getWidth() / 2);
		pane.setLayoutY(position.getY() - pane.getHeight() / 2);
	}

	@Nonnull
	public Vector2D getPosition() {
		return position;
	}

	@Nonnull
	private Vector2D destination = Vector2D.ZERO;

	public void setDestination(@Nonnull Vector2D destination) {
		this.destination = destination;
	}

	@Nonnull
	public Vector2D getDestination() {
		return destination;
	}

	private double speed = 1;

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	private double hitPoints = 1;

	public double getHitPoints() {
		return hitPoints;
	}

	public void setHitPoints(double hitPoints) {
		this.hitPoints = hitPoints;
	}

	private double armor = 1;

	public double getArmor() {
		return armor;
	}

	public void setArmor(double armor) {
		this.armor = armor;
	}

	@Nonnull
	@Override
	public Game getParent() {
		return player.getParent();
	}

	@Nonnull
	@Override
	public Pane getGraphic() {
		return pane;
	}

	@Nonnull
	@Override
	public Pane getIcon() {
		return icon;
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
		Vector2D difference = getDestination().subtract(getPosition());
		if (difference.magnitude() > 2) {
			Vector2D next = getPosition().add(difference.normalize().multiply(speed));
			Level level = getParent().getLevel();
			if (!level.getField(next.getX(), next.getY()).isBlocked()) {
				hide();
				setPosition(next);
			}
		}
		reveal();
	}

	public void hide() {
		Level level = getParent().getLevel();
		for (int x = -3; x < 4; x++) {
			for (int y = -3; y < 4; y++) {
				level.getField(position.getX()+x*16, position.getY()+y*16)
						.setVisible(false);
			}
		}
	}

	public void reveal() {
		Level level = getParent().getLevel();
		for (int x = -3; x < 4; x++) {
			for (int y = -3; y < 4; y++) {
				level.getField(position.getX()+x*16, position.getY()+y*16)
						.setVisible(true);
			}
		}
	}
}
