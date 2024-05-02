package org.nemesis;

import javafx.application.Application;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.nemesis.game.Game;
import org.nemesis.menu.Menu;

public class App extends Application {

	@Override
	public void start(Stage stage) {
		stage.setScene(new Game(new BorderPane()));
		stage.show();
	}
}
