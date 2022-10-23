package org.nemesis.grpc;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.nemesis.game.Game;
import org.nemesis.grpc.GameListingGrpc.GameListingImplBase;
import org.nemesis.grpc.NemesisGrpc.NemesisImplBase;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

public class RemoteServer {
	private static final Logger logger = Logger.getLogger(RemoteServer.class.getName());

	private final Map<String, Game> games = new HashMap<>();

	private final Server server;

	public RemoteServer() {
		server = ServerBuilder.forPort(7777).addService(new GameListingService()).addService(new NemesisService())
				.build();
		try {
			start();
		} catch (Exception e) {
			logger.warning(e.getMessage());
		}
		clock();
	}

	private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);

	boolean running = true;

	public void clock() {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				while (running) {
					try {
						Thread.sleep(5);
					} catch (InterruptedException e) {
						logger.warning(e.getMessage());
					}
				}
				try {
					stop();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void start() throws Exception {
		server.start();
		logger.info("Server started, listening on " + server.getPort());
	}

	public void stop() throws Exception {
		if (server != null) {
			server.shutdown().awaitTermination(5, TimeUnit.SECONDS);
			executor.shutdown();
			logger.info("Server shutdown");
		}
	}

	private class GameListingService extends GameListingImplBase {
		@Override
		public void hostGame(HostRequest request, StreamObserver<HostReply> responseObserver) {

		}

		@Override
		public void listGame(ListRequest request, StreamObserver<ListReply> responseObserver) {

		}

		@Override
		public void joinGame(JoinRequest request, StreamObserver<JoinReply> responseObserver) {

		}
	}

	private class NemesisService extends NemesisImplBase {

	}
}
