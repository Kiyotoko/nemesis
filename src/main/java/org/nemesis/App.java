package org.nemesis;

import javafx.application.Application;
import javafx.stage.Stage;
import org.nemesis.menu.Menu;

public class App extends Application {

	@Override
	public void start(Stage stage) {
		Menu menu = new Menu(stage);

		stage.setScene(menu);
		stage.show();
	}
}
