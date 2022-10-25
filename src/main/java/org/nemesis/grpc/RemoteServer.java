package org.nemesis.grpc;

import static org.nemesis.grpc.Corresponding.transform;

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

    public RemoteServer(int port) {
        server = ServerBuilder.forPort(port).addService(new NemesisService())
                .build();
        try {
            start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean running = true;

    public void start() throws Exception {
        server.start();
        game.getClock().start();
        logger.info("Server started, listening on " + server.getPort());
    }

    public void stop() throws Exception {
        game.getClock().markAsDone();
        server.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        logger.info("Server shutdown");
    }

    private class NemesisService extends NemesisImplBase {
        @Override
        public void gameStatus(StatusRequest request, StreamObserver<StatusReply> responseObserver) {
            responseObserver
                    .onNext(StatusReply.newBuilder().addAllPlayers(transform(game.getPlayers().values()))
                            .addAllRegions(transform(game.getRegions().values())).build());
            responseObserver.onCompleted();
        }
    }
}
