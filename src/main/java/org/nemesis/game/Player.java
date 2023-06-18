package org.nemesis.game;

import java.util.ArrayList;
import java.util.List;

import io.scvis.entity.Children;
import io.scvis.proto.Identifiable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Player implements Children, Identifiable, Displayable {
	private final List<Unit> units = new ArrayList<>();

	private final Label label = new Label(getClass().getSimpleName());
	{
		label.setFont(Font.font("Ubuntu", FontWeight.BOLD, 14));
		label.setTextFill(Color.WHITE);
		label.setPrefWidth(100);
		label.setPadding(new Insets(5));
		label.setBorder(new Border(
				new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, new CornerRadii(50), new BorderWidths(3))));
	}

	private final Party party;

	private Color color = Color.GREENYELLOW;
	private String name = "";

	public Player(Party party) {
		this.party = party;
		party.getPlayers().add(this);
		getParent().getPlayers().add(this);
		getParent().getChildren().add(this);
	}

	@Override
	public void update(double deltaT) {

	}

	@Override
	public void destroy() {
		Children.super.destroy();
		party.getPlayers().remove(this);
		getParent().getPlayers().remove(this);
	}

	public List<Unit> getUnits() {
		return units;
	}

	public Party getParty() {
		return party;
	}

	@Override
	public Game getParent() {
		return party.getParent();
	}

	@Override
	public Node getGraphic() {
		return label;
	}

	public void setColor(Color color) {
		this.color = color;
		label.setTextFill(color);
		label.setBorder(
				new Border(new BorderStroke(color, BorderStrokeStyle.SOLID, new CornerRadii(50), new BorderWidths(3))));
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
