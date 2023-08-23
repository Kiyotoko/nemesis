package org.nemesis;

import io.scvis.geometry.Vector2D;
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
			new BaseUnit(player, new Vector2D(128.0, 128.0 + 32.0 * i));

		Player computer = new Player(game);
		computer.setName("Computer");
		computer.setColor(Color.RED);
		for (int i = 0; i < 5; i++)
			new BaseUnit(computer, new Vector2D(512.0, 512.0 - 32.0 * i));

		stage.setScene(game);
		stage.show();
	}
}
