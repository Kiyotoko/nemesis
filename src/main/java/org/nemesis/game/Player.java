package org.nemesis.game;

import java.util.ArrayList;
import java.util.List;

import io.scvis.entity.Children;
import io.scvis.proto.Identifiable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import javax.annotation.Nonnull;

public class Player implements Children, Identifiable, Displayable {

	private static Player controller;

	private final @Nonnull List<Unit> units = new ArrayList<>();

	private final @Nonnull Label label = new Label(getClass().getSimpleName());
	{
		label.setFont(Font.font("Ubuntu", FontWeight.BOLD, 14));
		label.setTextFill(Color.WHITE);
	}

	private final Game game;

	private Color color = Color.GREENYELLOW;
	private String name = "";

	public Player(Game game) {
		this.game = game;
		getParent().getPlayers().add(this);
		getParent().getChildren().add(this);
	}

	@Override
	public void update(double deltaT) {
		// Nothing to update
		label.setText(getName() + " [" + getUnits().size() + "]");
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

	boolean isController() {
		return getController() == this;
	}

	public static Player getController() {
		return controller;
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

	public void setColor(Color color) {
		this.color = color;
		label.setGraphic(new Circle(7, color));
	}

	public Color getColor() {
		return color;
	}

	public void setName(String name) {
		this.name = name;
		label.setText(name);
	}

	public String getName() {
		return name;
	}
}
