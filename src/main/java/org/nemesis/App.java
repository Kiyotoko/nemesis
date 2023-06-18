package org.nemesis;

import org.nemesis.game.Game;
import org.nemesis.game.Party;
import org.nemesis.game.Player;
import org.nemesis.game.Unit;

import javafx.application.Application;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class App extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		Game game = new Game(new BorderPane());

		Party party = new Party(game);
		Player player = new Player(party);
		player.setName("Dragon");
		player.setColor(Color.RED);
		Unit unit = new Unit(player, null);
		unit.getGraphic().getChildren().add(new Circle(12, Color.WHITE));
		unit.getIcon().getChildren().add(new Circle(8, Color.WHITE));

		stage.setScene(game);
		stage.show();
	}
}
