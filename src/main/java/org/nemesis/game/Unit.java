package org.nemesis.game;

import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;

public class Unit extends Physical implements Iconable {

	private final @Nonnull Pane icon = new Pane();

	private @Nullable PathAnimation animation;

	public Unit(@Nonnull Player player, @Nonnull Point2D position) {
		super(player, position);
		addEventHandler();
		getGraphic().setVisible(getPlayer().isController());
		setDestination(position);
		if (getPlayer().isController()) {
			animation = new PathAnimation(this);
		}

		getPlayer().getUnits().add(this);
		getGame().getUnits().add(this);
	}

	@Override
	public void update() {
		super.update();
		shoot();
	}

	@Override
	public void displacement() {
		if (!getDestinations().isEmpty()) {
			Point2D difference = getDestination().subtract(getPosition());
			if (difference.magnitude() > getSpeed()) {
				Point2D next = getPosition().add(difference.normalize().multiply(getSpeed()));
				if (isPositionAvailable(next)) setPosition(next);
			} else getDestinations().remove();
		}
		if (!getPlayer().isController())
			visible();
	}

	public void shoot() {
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
		setReloadTime(Math.max(getReloadTime() - 1, 0));
	}

	public void visible() {
		Field field = getGame().getLevel().getField(getPosition().getX(), getPosition().getY());
		if (field == null) return;
		double difference = field.getSightDistance() * 16.0 + 2 * 12.0;
		for (Unit unit : List.copyOf(getGame().getUnits())) {
			if (unit.getPlayer() != getPlayer() && (Math.abs(unit.getPosition().getX() - getPosition().getX()) <
					difference && Math.abs(unit.getPosition().getY() - getPosition().getY()) < difference)) {
				getGraphic().setVisible(true);
				return;
			}
		}
		getGraphic().setVisible(false);
	}

	public void reveal() {
		Level level = getGame().getLevel();
		Field inside = level.getField(getPosition().getX(), getPosition().getY());
		if (inside == null) return;
		double visibility = inside.getSightDistance();
		for (double x = -visibility; x < visibility; x++) {
			for (double y = -visibility; y < visibility; y++) {
				Field field = level.getField(getPosition().getX()+x*16, getPosition().getY()+y*16);
				if (field != null) field.setHidden(true);
			}
		}
	}

	@Override
	public void destroy() {
		getPlayer().getUnits().remove(this);
		getGame().getUnits().remove(this);
		if (getPlayer().isController()) {
			getGame().getSelected().remove(this);
			if (animation != null)
				animation.destroy();
		}
	}

	@Override
	public void relocate() {
		super.relocate();
		if (getPlayer().isController())
			reveal();
	}

	public void select() {
		// For override
	}

	public void deselect() {
		// For override
	}

	private void addEventHandler() {
		Player player = getPlayer();
		getGraphic().setPickOnBounds(true);
		getGraphic().addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
			if (e.getButton() == MouseButton.PRIMARY) {
				if (getPlayer().isController()) {
					if (!e.isShiftDown())
						player.getGame().getSelected().clear();
					player.getGame().getSelected().add(this);
				}
			} else if (e.getButton() == MouseButton.SECONDARY && !player.isController()) {
					getGame().getSelected().forEach(unit -> unit.setTarget(this));
			}
		});
		getIcon().addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
			if (e.getButton() == MouseButton.PRIMARY) {
				player.getGame().getSelected().clear();
				player.getGame().getSelected().add(this);
			} else if (e.getButton() == MouseButton.SECONDARY) {
				player.getGame().getSelected().remove(this);
			}
		});
	}

	public boolean isPositionAvailable(Point2D next) {
		Field field = getGame().getLevel().getField(next.getX(), next.getY());
		if (field != null && !field.isBlocked()) {
			for (Unit unit : getPlayer().getGame().getUnits()) {
				if (unit != this && unit.getPosition().distance(next) < 16) return false;
			}
			return true;
		}
		return false;
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

	public static final int UNMARKED = 0;

	private int mark = UNMARKED;

	public void setMark(int mark) {
		this.mark = mark;
	}

	public int getMark() {
		return mark;
	}
}
