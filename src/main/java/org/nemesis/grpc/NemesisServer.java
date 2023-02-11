package org.nemesis.grpc;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.nemesis.game.Game;
import org.nemesis.game.Party;
import org.nemesis.game.Player;
import org.nemesis.game.Projectile;
import org.nemesis.game.Unit;

import com.google.common.hash.Hashing;
import com.karlz.bounds.Vector;
import com.karlz.exchange.Corresponding;
import com.karlz.exchange.ExchangeHelper.DispatchHelper;
import com.karlz.exchange.Observable;
import com.karlz.grpc.exchange.HostRequest;
import com.karlz.grpc.exchange.HostResponse;
import com.karlz.grpc.exchange.HostingGrpc.HostingImplBase;
import com.karlz.grpc.exchange.JoinRequest;
import com.karlz.grpc.exchange.JoinResponse;
import com.karlz.grpc.exchange.ListRequest;
import com.karlz.grpc.exchange.ListResponse;
import com.karlz.grpc.exchange.PingRequest;
import com.karlz.grpc.exchange.PingResponse;
import com.karlz.grpc.game.ChangeRequest;
import com.karlz.grpc.game.ChangeResponse;
import com.karlz.grpc.game.NemesisGrpc.NemesisImplBase;
import com.karlz.grpc.game.StatusRequest;
import com.karlz.grpc.game.StatusResponse;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.Status;
import io.grpc.StatusException;
import io.grpc.stub.StreamObserver;

public class NemesisServer {
    private final Server server;

    public NemesisServer(int port) {
        server = ServerBuilder.forPort(port).addService(new NemesisService()).addService(new HostingService()).build();
    }

    public void start() {
        try {
            server.start();
            game.getClock().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        game.getClock().markAsDone();
        server.shutdown();
    }

    public class NemesisDispatchHelper implements DispatchHelper<Corresponding<?>, StatusResponse> {
        private final Set<Party> parties = new HashSet<>();
        private final Set<Player> players = new HashSet<>();
        private final Set<Unit> units = new HashSet<>();
        private final Set<Projectile> projectiles = new HashSet<>();
        private final Set<Observable> destroyed = new HashSet<>();

        public NemesisDispatchHelper(Game game) {
            init(game);
        }

        @SuppressWarnings("unchecked")
        @Override
        public StatusResponse associated() {
            return StatusResponse.newBuilder()
                    .addAllParties((Iterable<com.karlz.grpc.game.Party>) (Iterable<?>) Corresponding.transform(parties))
                    .addAllPlayers(
                            (Iterable<com.karlz.grpc.game.Player>) (Iterable<?>) Corresponding.transform(players))
                    .addAllUnits((Iterable<com.karlz.grpc.game.Unit>) (Iterable<?>) Corresponding.transform(units))
                    .addAllProjectiles((Iterable<com.karlz.grpc.game.Projectile>) (Iterable<?>) Corresponding
                            .transform(projectiles))
                    .addAllDestroyed((Iterable<com.karlz.grpc.entity.Observable>) (Iterable<?>) Corresponding
                            .transform(destroyed))
                    .build();
        }

        public void init(Game game) {
            parties.addAll(game.getParties());
            players.addAll(game.getPlayers());
            units.addAll(game.getUnits());
            projectiles.addAll(game.getProjectiles());
        }

        @Override
        public void clean() {
            parties.clear();
            players.clear();
            units.clear();
            projectiles.clear();
            destroyed.clear();
        }

        @Override
        public void push(Collection<Corresponding<?>> collection, Corresponding<?> element) {
            collection.add(element);
        }

        public Set<Party> getParties() {
            return parties;
        }

        public Set<Player> getPlayers() {
            return players;
        }

        public Set<Unit> getUnits() {
            return units;
        }

        public Set<Projectile> getProjectiles() {
            return projectiles;
        }

        public Set<Observable> getDestroyed() {
            return destroyed;
        }
    }

    private final Game game = new Game(860, 680);

    protected class NemesisService extends NemesisImplBase {
        @Override
        public void status(StatusRequest request, StreamObserver<StatusResponse> responseObserver) {
            NemesisDispatchHelper helper = game.getDispatchers().get(request.getToken());
            if (helper != null) {
                responseObserver.onNext(helper.dispatch());
                responseObserver.onCompleted();
            } else {
                responseObserver.onError(new StatusException(Status.PERMISSION_DENIED));
            }
        }

        @Override
        public void change(ChangeRequest request, StreamObserver<ChangeResponse> responseObserver) {
            Player controller = game.getControllers().get(request.getToken());
            if (controller != null) {
                Unit target = controller.getControllerTargets().get(request.getUnitId());
                if (target != null) {
                    target.setDestination(new Vector(request.getDestination().getX(), request.getDestination().getY()));
                    responseObserver.onNext(ChangeResponse.newBuilder()
                            .setMessage(controller.getId() + " -> " + target.getId()).build());
                    responseObserver.onCompleted();
                } else {
                    responseObserver.onError(new StatusException(Status.NOT_FOUND));
                }
            } else {
                responseObserver.onError(new StatusException(Status.PERMISSION_DENIED));
            }
        }
    }

    protected class HostingService extends HostingImplBase {
        private static String getNewToken() {
            return Hashing.sha512().hashLong(System.currentTimeMillis()).toString();
        }

        @Override
        public void join(JoinRequest request, StreamObserver<JoinResponse> responseObserver) {
            String token = getNewToken();

            Player player = new Player(new Party(game));
            player.setUserName(request.getDetailsMap().get("user_name"));
            player.setWebColor(request.getDetailsMap().get("web_color"));
            game.getControllers().put(token, player);
            game.getDispatchers().put(token, new NemesisDispatchHelper(game));

            responseObserver.onNext(JoinResponse.newBuilder().setToken(token).build());
            responseObserver.onCompleted();
        }

        @Override
        public void host(HostRequest request, StreamObserver<HostResponse> responseObserver) {
            responseObserver.onError(new StatusException(Status.UNIMPLEMENTED));
        }

        @Override
        public void list(ListRequest request, StreamObserver<ListResponse> responseObserver) {
            responseObserver.onError(new StatusException(Status.UNIMPLEMENTED));
        }

        @Override
        public void ping(PingRequest request, StreamObserver<PingResponse> responseObserver) {
            responseObserver.onNext(PingResponse.newBuilder().setPing(System.currentTimeMillis()).build());
            responseObserver.onCompleted();
        }
    }
}
