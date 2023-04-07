package org.nemesis.graphic;

import com.karlz.exchange.Mirror;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Party extends Mirror<com.karlz.grpc.game.Party> {
	private final Game game;

	public Party(Game game) {
		super(new Label("Party { }", new Circle(5, Color.CRIMSON)));
		this.game = game;
		getReflection().setTextFill(Color.WHITE);
	}

	private final ObservableList<String> playerIds = FXCollections.observableArrayList();

	@Override
	public void update(com.karlz.grpc.game.Party reference) {
		if (!hasSource())
			setSource(reference.getSuper());

		playerIds.setAll(reference.getPlayerIdsList());
	}

	@Override
	public Label getReflection() {
		return (Label) super.getReflection();
	}

	public Game getGame() {
		return game;
	}

	public ObservableList<String> getPlayerIds() {
		return playerIds;
	}
}
