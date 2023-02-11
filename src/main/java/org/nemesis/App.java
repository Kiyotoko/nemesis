package org.nemesis;

import org.nemesis.grpc.NemesisClient;
import org.nemesis.grpc.NemesisServer;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
	private final NemesisServer server = new NemesisServer(9876);
	private final NemesisClient client = new NemesisClient("localhost", 9876);

	@Override
	public void init() throws Exception {
		server.start();
		client.start();
		new AnimationTimer() {
			final long[] frameTimes = new long[100];
			int frameTimeIndex = 0;
			boolean arrayFilled = false;

			@Override
			public void handle(long now) {
				frameTimes[frameTimeIndex] = now;
				frameTimeIndex = (frameTimeIndex + 1) % frameTimes.length;
				if (frameTimeIndex == 0) {
					arrayFilled = true;
				}
				if (arrayFilled) {
					client.status();
				}
			}
		}.start();
	}

	@Override
	public void start(Stage stage) throws Exception {
		Scene scene = new Scene(client.getGame(), 860, 680);

		stage.setOnCloseRequest(c -> {
			server.stop();
			client.stop();
		});
		stage.setScene(scene);
		stage.show();
	}
}
