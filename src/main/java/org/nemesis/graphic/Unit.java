package org.nemesis.graphic;

import org.nemesis.grpc.Factory.GraphicFactory;

import com.karlz.exchange.Mirror;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

public class Unit extends Mirror<com.karlz.grpc.game.Unit> {
	private final Game game;

	public Unit(Game game) {
		super(new Pane());
		this.game = game;
		getReflection().addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
			if (e.getButton() == MouseButton.PRIMARY) {
				if (game.getSelected().contains(this))
					return;
				if (!e.isShiftDown())
					game.getSelected().clear();
				game.getSelected().add(this);
			}
		});
	}

	private final ObjectProperty<Color> color = new SimpleObjectProperty<>(Color.RED);
	private final DoubleProperty velocity = new SimpleDoubleProperty();
	private final DoubleProperty hitPoints = new SimpleDoubleProperty();
	private final DoubleProperty shields = new SimpleDoubleProperty();
	private final DoubleProperty armor = new SimpleDoubleProperty();

	@Override
	public void update(com.karlz.grpc.game.Unit reference) {
		if (!hasSource()) {
			setSource(reference.getKinetic().getIdentifiable());
			Shape shape = GraphicFactory.FACTORY_MAP.get(reference.getModel()).apply(this);
			shape.fillProperty().bind(color);
			getReflection().getChildren().add(shape);
		}

		velocity.set(reference.getKinetic().getVelocity());
		hitPoints.set(reference.getHitPoints());
		shields.set(reference.getShields());
		armor.set(reference.getArmor());

		getReflection().setRotate(Math.toDegrees(reference.getKinetic().getLayout().getRotation()));
		getReflection().setLayoutX(reference.getKinetic().getLayout().getPosition().getX());
		getReflection().setLayoutY(reference.getKinetic().getLayout().getPosition().getY());
	}

	@Override
	public Pane getReflection() {
		return (Pane) super.getReflection();
	}

	public Game getGame() {
		return game;
	}

	private Circle icon;

	public Node getIcon() {
		if (icon == null) {
			icon = new Circle(8);
			icon.fillProperty().bind(color);
		}
		return icon;
	}

	public ObjectProperty<Color> getColor() {
		return color;
	}

	public DoubleProperty getVelocity() {
		return velocity;
	}

	public DoubleProperty getHitPoints() {
		return hitPoints;
	}

	public DoubleProperty getArmor() {
		return armor;
	}

	public DoubleProperty getShields() {
		return shields;
	}
}
