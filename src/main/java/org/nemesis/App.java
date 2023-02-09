package org.nemesis;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(new Group(), 786, 786);

        stage.setOnCloseRequest(c -> {

        });
        stage.setScene(scene);
        stage.show();
    }
}
