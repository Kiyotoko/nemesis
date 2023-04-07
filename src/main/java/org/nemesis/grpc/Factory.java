package org.nemesis.grpc;

import java.io.File;
import java.util.Map;
import java.util.function.Function;

import javax.annotation.Nonnull;

import org.nemesis.game.Player;
import org.nemesis.game.Projectile;
import org.nemesis.game.Unit;
import org.nemesis.grpc.FactoryResource.GraphicResource;
import org.nemesis.grpc.FactoryResource.ProjectileResource;
import org.nemesis.grpc.FactoryResource.UnitResource;

import com.karlz.exchange.Mirror;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public abstract class Factory<I, O> implements Function<I, O> {

	private final FactoryResource resource;

	public Factory(@Nonnull File source, @Nonnull Class<? extends FactoryResource> clazz) {
		this(FactoryResource.load(source, clazz));
	}

	public Factory(@Nonnull FactoryResource resource) {
		this.resource = resource;
	}

	public static class UnitFactory extends Factory<Player, Unit> {

		public static final UnitFactory INFANTERY = new UnitFactory(
				FactoryResource.load(new File("src/main/resources/nemesis/unit/infantery.json"), UnitResource.class));
		public static final UnitFactory ARTILLERY = new UnitFactory(
				FactoryResource.load(new File("src/main/resources/nemesis/unit/artillery.json"), UnitResource.class));
		public static final UnitFactory TANK = new UnitFactory(
				FactoryResource.load(new File("src/main/resources/nemesis/unit/tank.json"), UnitResource.class));
		public static final UnitFactory AIRCRAFT = new UnitFactory(
				FactoryResource.load(new File("src/main/resources/nemesis/unit/aircraft.json"), UnitResource.class));

		public static final Map<String, UnitFactory> FACTORY_MAP = Map.of("infantery", INFANTERY, "artillery",
				ARTILLERY, "tank", TANK, "aircraft", AIRCRAFT);

		public UnitFactory(@Nonnull File source) {
			super(source, UnitResource.class);
		}

		public UnitFactory(@Nonnull UnitResource resource) {
			super(resource);
		}

		@Override
		public Unit apply(Player t) {
			UnitResource resource = (UnitResource) getResource();
			Unit unit = new Unit(t, resource.getLayout().clone(), resource.getModel(), 0.0, resource.getAcceleration(),
					resource.getVelocity()) {
				@Override
				public String getType() {
					return resource.getModel();
				}
			};
			unit.setCircularSpeed(resource.getCircularSpeed());
			unit.setMovingAfterRotation(resource.getMoveAfterRotation());
			unit.setHitPoints(resource.getHitPoints());
			unit.setArmor(resource.getArmor());
			unit.setShields(resource.getShields());
			return unit;
		}
	}

	public static class ProjectileFactory extends Factory<Unit, Projectile> {
		public ProjectileFactory(@Nonnull File source) {
			super(source, ProjectileResource.class);
		}

		public ProjectileFactory(@Nonnull UnitResource resource) {
			super(resource);
		}

		@Override
		public Projectile apply(Unit t) {
			ProjectileResource resource = (ProjectileResource) getResource();
			Projectile projectile = new Projectile(t.getPlayer().getParty(), resource.getLayout().clone(),
					resource.getMass());
			projectile.setPosition(t.getPosition());
			projectile.setSpeed(resource.getSpeed());
			projectile.setRange(resource.getRange());
			projectile.setDamage(resource.getDamage());
			projectile.setCriticalChance(resource.getCriticalChance());
			projectile.setCriticalDamage(resource.getCriticalDamage());
			return projectile;
		}
	}

	public static class GraphicFactory extends Factory<Mirror<?>, Shape> {

		public static final GraphicFactory INFANTERY = new GraphicFactory(new GraphicResource(p -> new Circle(6)));
		public static final GraphicFactory ARTILLERY = new GraphicFactory(
				new GraphicResource(p -> new Polygon(-6, -8, 6, -8, 10, 0, 6, 8, -6, 8, -10, 0)));
		public static final GraphicFactory TANK = new GraphicFactory(new GraphicResource(p -> new Rectangle(18, 10)));
		public static final GraphicFactory AIRCRAFT = new GraphicFactory(
				new GraphicResource(p -> new Polygon(-8, -6, 8, 0, -8, 6)));

		public static final Map<String, GraphicFactory> FACTORY_MAP = Map.of("infantery", INFANTERY, "artillery",
				ARTILLERY, "tank", TANK, "aircraft", AIRCRAFT);

		public GraphicFactory(GraphicResource resource) {
			super(resource);
		}

		@Override
		public Shape apply(Mirror<?> t) {
			return ((GraphicResource) getResource()).getFunction().apply(t);
		}
	}

	public FactoryResource getResource() {
		return resource;
	}
}
