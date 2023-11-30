package org.nemesis;

import org.nemesis.game.Game;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

	@Override
	public void start(Stage stage) {
		Game game = new Game(new Game.GameSettings("level.json"));

		stage.setScene(game);
		stage.show();
	}
}
