package org.nemesis.game;

import java.util.List;
import java.util.Set;

import io.scvis.entity.Children;
import io.scvis.proto.Identifiable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Player implements Children, Identifiable, Displayable {

	private static @Nullable Player controller;

	private final @Nonnull ObservableList<Unit> units = FXCollections.observableArrayList();

	private final @Nonnull Label label = new Label(getClass().getSimpleName());

	private final @Nonnull Game game;

	private @Nonnull Color color = Color.GREENYELLOW;
	private @Nonnull String name = "Unknown";

	public Player(@Nonnull Game game) {
		this.game = game;
		label.setFont(Font.font("Ubuntu", FontWeight.BOLD, 14));
		label.setTextFill(Color.WHITE);

		ListChangeListener<Unit> listener = change -> {
			change.next();
			if (change.getList().isEmpty()) destroy();
			else label.setText(getName() + " [" + change.getList().size() + "]");
		};
		units.addListener(listener);

		getParent().getPlayers().add(this);
		getParent().getChildren().add(this);
	}

	@Override
	public void update(double deltaT) {
		// Nothing to update
	}

	@Override
	public void destroy() {
		Children.super.destroy();
		getGame().getPlayers().remove(this);
		getParent().getChildren().remove(this);
	}

	public void markAsController() {
		controller = this;
	}

	public boolean isController() {
		return getController() == this;
	}

	@Nullable
	public static Player getController() {
		return controller;
	}

	private static void mark(int mark) {
		if (getController() != null) {
			for (Unit unit : getController().getUnits())
				if (unit.getMark() == mark) unit.setMark(Unit.UNMARKED);
			Set<Unit> selected = getController().getParent().getSelected();
			for (Unit unit : selected) {
				unit.setMark(mark);
			}
		}
	}

	private static void select(int mark) {
		if (getController() != null) {
			Set<Unit> selected = getController().getParent().getSelected();
			selected.forEach(Unit::deselect);
			for (Unit unit : getController().getUnits())
				if (unit.getMark() == mark) unit.select();
		}
	}

	public static EventHandler<KeyEvent> getKeyEventHandler() {
		return e -> {
			if (getController() != null) {
				if (e.isControlDown()) {
					switch (e.getCode()) {
						case A:
							getController().getUnits().forEach(Unit::select);
							break;
						case S:
							getController().getParent().getSelected().forEach(unit ->
								unit.setDestination(unit.getPosition()));
							break;
						case D:
							getController().getParent().getSelected().forEach(unit ->
								unit.setTarget(null));
							break;
						case DIGIT1:
							mark(1);
							break;
						case DIGIT2:
							mark(2);
							break;
						case DIGIT3:
							mark(3);
							break;
						case DIGIT4:
							mark(4);
							break;
						case DIGIT5:
							mark(5);
							break;
						case DIGIT6:
							mark(6);
							break;
						case DIGIT7:
							mark(7);
							break;
						case DIGIT8:
							mark(8);
							break;
						case DIGIT9:
							mark(9);
							break;
						case DIGIT0:
							mark(0);
							break;
						default: break;
					}
				} else {
					switch (e.getCode()) {
						case DIGIT1:
							select(1);
							break;
						case DIGIT2:
							select(2);
							break;
						case DIGIT3:
							select(3);
							break;
						case DIGIT4:
							select(4);
							break;
						case DIGIT5:
							select(5);
							break;
						case DIGIT6:
							select(6);
							break;
						case DIGIT7:
							select(7);
							break;
						case DIGIT8:
							select(8);
							break;
						case DIGIT9:
							select(9);
							break;
						case DIGIT0:
							select(0);
							break;
						default:
							break;
					}
				}
			}
		};
	}

	@Nonnull
	public List<Unit> getUnits() {
		return units;
	}

	@Nonnull
	@Override
	public Game getParent() {
		return getGame();
	}

	@Nonnull
	public Game getGame() {
		return game;
	}

	@Nonnull
    @Override
	public Node getGraphic() {
		return label;
	}

	public void setColor(@Nonnull Color color) {
		this.color = color;
		label.setGraphic(new Circle(7, color));
	}

	@Nonnull
	public Color getColor() {
		return color;
	}

	public void setName(@Nonnull String name) {
		this.name = name;
		label.setText(name);
	}

	@Nonnull
	public String getName() {
		return name;
	}
}
