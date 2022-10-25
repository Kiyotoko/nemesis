package org.nemesis.grpc;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.nemesis.game.Game;
import org.nemesis.grpc.NemesisGrpc.NemesisImplBase;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

public class RemoteServer {
    private static final Logger logger = Logger.getLogger(RemoteServer.class.getName());

    private final Server server;

    private Game game = new Game();

    public RemoteServer() {
        server = ServerBuilder.forPort(7777).addService(new NemesisService())
                .build();
        try {
            start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        game.getClock().start();
    }

    private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);

    boolean running = true;

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

    private class NemesisService extends NemesisImplBase {
        @Override
        public void gameStatus(StatusRequest request, StreamObserver<StatusReply> responseObserver) {
            responseObserver.onNext(StatusReply.newBuilder().build());
            responseObserver.onCompleted();
        }
    }
}
