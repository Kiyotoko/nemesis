package org.nemesis;

import javafx.geometry.Point2D;
import org.nemesis.content.BaseUnit;
import org.nemesis.game.ControlPoint;
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
		game.setOnKeyPressed(Player.getKeyEventHandler());
		for (int i = 1; i < 3; i++)
			new ControlPoint(game, new Point2D(game.getLevel().getWidth() * i * 0.33333, game.getLevel().getHeight() * 0.5));

		Player player = new Player(game);
		player.markAsController();
		player.setName("Player");
		player.setColor(Color.LIGHTBLUE);
		for (int i = -2; i < 3; i++)
			new BaseUnit(player, new Point2D(game.getLevel().getWidth() * 0.5 + 48.0 * i, game.getLevel().getHeight() * 0.15));

		Player computer = new Player(game);
		computer.setName("Computer");
		computer.setColor(Color.ORANGERED);
		for (int i = -2; i < 3; i++)
			new BaseUnit(computer, new Point2D(game.getLevel().getWidth() * 0.5 + 48.0 * i, game.getLevel().getHeight() * 0.85));

		stage.setScene(game);
		stage.show();
	}
}
