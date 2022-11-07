package org.nemesis.grpc;

import static org.nemesis.grpc.Corresponding.transform;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.nemesis.game.Game;
import org.nemesis.game.Region;
import org.nemesis.grpc.NemesisGrpc.NemesisImplBase;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.Status;
import io.grpc.StatusException;
import io.grpc.stub.StreamObserver;

public class NemesisServer {
    private static final Logger logger = Logger.getLogger(NemesisServer.class.getName());

    private final Server server;

    private Game game = new Game();

    public NemesisServer(int port) {
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

        @Override
        public void changeEconomy(ChangeEconomyRequest request, StreamObserver<ChangeReply> responseObserver) {
            Region region = game.getRegions().get(
                    request.getLocation());

            if (region != null) {
                boolean changed = false;
                if (request.getUpgrad()) {
                    changed |= region.upgradeEconomy();
                }
                responseObserver.onNext(ChangeReply.newBuilder().setChanged(changed).build());
            } else {
                responseObserver.onError(new StatusException(Status.NOT_FOUND));
            }
            responseObserver.onCompleted();
        }

        @Override
        public void changeMilitary(ChangeMilitaryRequest request, StreamObserver<ChangeReply> responseObserver) {
            Region region = game.getRegions().get(
                    request.getLocation());

            if (region != null) {
                boolean changed = false;
                if (request.getUpgrad()) {
                    changed |= region.upgradeMilitary();
                }
                responseObserver.onNext(ChangeReply.newBuilder().setChanged(changed).build());
            } else {
                responseObserver.onError(new StatusException(Status.NOT_FOUND));
            }
            responseObserver.onCompleted();
        }

        @Override
        public void changeDevense(ChangeDevenseRequest request, StreamObserver<ChangeReply> responseObserver) {
            Region region = game.getRegions().get(
                    request.getLocation());

            if (region != null) {
                boolean changed = false;
                if (request.getUpgrad()) {
                    changed |= region.upgradeDevense();
                }
                responseObserver.onNext(ChangeReply.newBuilder().setChanged(changed).build());
            } else {
                responseObserver.onError(new StatusException(Status.NOT_FOUND));
            }
            responseObserver.onCompleted();
        }
    }
}
