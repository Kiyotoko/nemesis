package org.nemesis;

import org.nemesis.content.BaseUnit;
import org.nemesis.game.Game;
import org.nemesis.game.Player;

import javafx.application.Application;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class App extends Application {

	@Override
	public void start(Stage stage) {
		Game game = new Game(new BorderPane());

		Player player = new Player(game);
		player.markAsController();
		player.setName("Player");
		player.setColor(Color.BLUE);
		for (int i = 0; i < 5; i++)
			new BaseUnit(player);


		Player computer = new Player(game);
		computer.setName("Computer");
		computer.setColor(Color.RED);
		for (int i = 0; i < 5; i++)
			new BaseUnit(computer);

		stage.setScene(game);
		stage.show();
	}
}
