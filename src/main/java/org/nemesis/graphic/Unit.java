package org.nemesis.graphic;

import org.nemesis.grpc.Factory.GraphicFactory;

import com.karlz.exchange.Mirror;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
				if (selected.get())
					return;
				if (!e.isShiftDown())
					game.getSelected().clear();
				selected.set(true);
			}
		});
	}

	private final ObjectProperty<Color> color = new SimpleObjectProperty<>(Color.BLACK);
	private final StringProperty model = new SimpleStringProperty() {
		protected void invalidated() {
			Shape shape = GraphicFactory.FACTORY_MAP.get(get()).apply(Unit.this);
			shape.fillProperty().bind(color);
			getReflection().getChildren().add(shape);
		};
	};
	private final BooleanProperty selected = new SimpleBooleanProperty(false) {
		protected void invalidated() {
			if (get()) {
				if (!getGame().getSelected().contains(Unit.this))
					getGame().getSelected().add(Unit.this);
			} else
				getGame().getSelected().remove(Unit.this);
		}
	};
	private final DoubleProperty velocity = new SimpleDoubleProperty();
	private final DoubleProperty hitPoints = new SimpleDoubleProperty();
	private final DoubleProperty shields = new SimpleDoubleProperty();
	private final DoubleProperty armor = new SimpleDoubleProperty();

	@Override
	public void update(com.karlz.grpc.game.Unit reference) {
		if (!hasSource()) {
			setSource(reference.getKinetic().getIdentifiable());
		}

		model.set(reference.getModel());
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

	private Node icon;

	public Node getIcon() {
		if (icon == null) {
			icon = new Pane(new Circle(5, Color.CRIMSON));

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
