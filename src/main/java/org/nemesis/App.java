package org.nemesis;

import org.nemesis.grpc.NemesisClient;
import org.nemesis.grpc.NemesisServer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    private final NemesisServer server = new NemesisServer(8080);
    private final NemesisClient client = new NemesisClient("localhost", 8080);

    @Override
    public void start(Stage stage) throws Exception {
        server.start();

        Scene scene = new Scene(client.getGame(), 786, 786);

        stage.setOnCloseRequest(c -> {

        });
        stage.setScene(scene);
        stage.show();
    }
}
