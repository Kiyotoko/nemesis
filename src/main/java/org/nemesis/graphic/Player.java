package org.nemesis.graphic;

import com.karlz.exchange.Mirror;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Player extends Mirror<com.karlz.grpc.game.Player> {
	private final Game game;

	public Player(Game game) {
		super(new Label("Player { }", new Circle(5, Color.AQUAMARINE)));
		this.game = game;
		getReflection().setTextFill(Color.WHITE);
		webColor.addListener((observable, o, n) -> getReflection().setGraphic(new Circle(5, Color.web(n))));
		userName.addListener((observable, o, n) -> getReflection().setText(n));
	}

	private final ObservableList<String> unitIds = FXCollections.observableArrayList();
	private final StringProperty webColor = new SimpleStringProperty("");
	private final StringProperty userName = new SimpleStringProperty("");

	@Override
	public void update(com.karlz.grpc.game.Player reference) {
		if (!hasSource())
			setSource(reference.getSuper());

		unitIds.setAll(reference.getUnitIdsList());
		webColor.set(reference.getWebColor());
		userName.set(reference.getUserName());
	}

	@Override
	public Label getReflection() {
		return (Label) super.getReflection();
	}

	public Game getGame() {
		return game;
	}

	public ObservableList<String> getUnitIds() {
		return unitIds;
	}
}
