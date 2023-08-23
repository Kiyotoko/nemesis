package org.nemesis.game;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import io.scvis.geometry.Vector2D;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.function.Function;

public class Unit extends Physical implements Iconifiable {

	private final @Nonnull Pane icon = new Pane();

	public Unit(@Nonnull Player player, @Nonnull Vector2D position) {
		super(player, position);
		getGraphic().addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
			if (e.getButton() == MouseButton.PRIMARY) {
				if (getPlayer().isController()) {
					if (!e.isShiftDown())
						player.getGame().getSelected().clear();
					player.getGame().getSelected().add(this);
				}
			} else if (e.getButton() == MouseButton.SECONDARY) {
				for (Unit unit: getParent().getSelected()) {
					unit.setTarget(this);
				}
			}
		});
		getGraphic().setPickOnBounds(true);
		setDestination(position);

		getPlayer().getUnits().add(this);
		getParent().getUnits().add(this);
	}

	@Override
	public void update(double deltaT) {
		super.update(deltaT);
		shoot(deltaT);
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
			Vector2D next = getPosition().add(difference.normalize().multiply(getSpeed()));
			Level level = getParent().getLevel();
			if (!level.getField(next.getX(), next.getY()).isBlocked()) {
				if (getPlayer().isController()) hide();
				setPosition(next);
			}
		}
		if (getPlayer().isController()) reveal();
	}

	public void shoot(double deltaT) {
		if (hasTarget() && !isReloading()) {
			if (getTarget().getHitPoints() > 0) {
				Function<Unit, Projectile> creator = getProjectileCreator();
				if (creator != null) {
					creator.apply(this);
					setReloadTime(getReloadSpeed());
				}
			} else {
				setTarget(null);
			}
		}
		setReloadTime(Math.max(getReloadTime() - deltaT, 0));
	}

	public void hide() {
		Level level = getParent().getLevel();
		for (int x = -3; x < 4; x++) {
			for (int y = -3; y < 4; y++) {
				level.getField(getPosition().getX()+x*16, getPosition().getY()+y*16)
						.setVisible(false);
			}
		}
	}

	public void reveal() {
		Level level = getParent().getLevel();
		for (int x = -3; x < 4; x++) {
			for (int y = -3; y < 4; y++) {
				level.getField(getPosition().getX()+x*16, getPosition().getY()+y*16)
						.setVisible(true);
			}
		}
	}

	@Override
	public void destroy() {
		super.destroy();
		getPlayer().getUnits().remove(this);
		getParent().getUnits().remove(this);
		if (getPlayer().isController())
			getParent().getSelected().remove(this);
	}

	@Nonnull
	@Override
	public Pane getIcon() {
		return icon;
	}

	private @Nullable Function<Unit, Projectile> projectileCreator;

	public void setProjectileCreator(@Nullable Function<Unit, Projectile> projectileCreator) {
		this.projectileCreator = projectileCreator;
	}

	@Nullable
	public Function<Unit, Projectile> getProjectileCreator() {
		return projectileCreator;
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

	private double hitPoints = 1;

	public double getHitPoints() {
		return hitPoints;
	}

	public void setHitPoints(double hitPoints) {
		this.hitPoints = hitPoints;
		if (hitPoints <= 0) destroy();
	}

	private double armor = 1;

	public double getArmor() {
		return armor;
	}

	public void setArmor(double armor) {
		this.armor = armor;
	}

	private double reloadTime = 0;

	public boolean isReloading() {
		return reloadTime > 0;
	}

	public void setReloadTime(double reloadTime) {
		this.reloadTime = reloadTime;
	}

	public double getReloadTime() {
		return reloadTime;
	}

	private double reloadSpeed = 1;

	public void setReloadSpeed(double reloadSpeed) {
		this.reloadSpeed = reloadSpeed;
	}

	public double getReloadSpeed() {
		return reloadSpeed;
	}
}
