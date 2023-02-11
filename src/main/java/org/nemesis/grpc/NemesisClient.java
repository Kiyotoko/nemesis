package org.nemesis.grpc;

import java.util.Map;

import org.nemesis.graphic.Game;

import com.karlz.entity.Creator;
import com.karlz.exchange.ExchangeHelper.StoreHelper;
import com.karlz.exchange.Reference;
import com.karlz.grpc.bounds.Vector;
import com.karlz.grpc.exchange.HostingGrpc;
import com.karlz.grpc.exchange.HostingGrpc.HostingBlockingStub;
import com.karlz.grpc.exchange.JoinRequest;
import com.karlz.grpc.exchange.PingRequest;
import com.karlz.grpc.game.ChangeRequest;
import com.karlz.grpc.game.NemesisGrpc;
import com.karlz.grpc.game.NemesisGrpc.NemesisBlockingStub;
import com.karlz.grpc.game.Party;
import com.karlz.grpc.game.Player;
import com.karlz.grpc.game.Projectile;
import com.karlz.grpc.game.StatusRequest;
import com.karlz.grpc.game.StatusResponse;
import com.karlz.grpc.game.Unit;

import io.grpc.ConnectivityState;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import javafx.geometry.Point2D;

public class NemesisClient {
    private final ManagedChannel channel;
    private final NemesisBlockingStub nemesisBlockingStub;
    private final HostingBlockingStub hostingBlockingStub;

    public NemesisClient(String name, int port) {
        channel = ManagedChannelBuilder.forAddress(name, port).usePlaintext().build();
        nemesisBlockingStub = NemesisGrpc.newBlockingStub(channel);
        hostingBlockingStub = HostingGrpc.newBlockingStub(channel);
    }

    public void start() {
        join();
    }

    public void stop() {
        channel.shutdown();
    }

    private final Game game = new Game(this);

    public class NemesisStoreHelper implements StoreHelper<Reference<?>, StatusResponse> {
        private final Map<String, Creator<? extends Reference<?>>> creators = Map.of( //
                "Party", () -> new org.nemesis.graphic.Party(getGame()), //
                "Player", () -> new org.nemesis.graphic.Player(getGame()), //
                "Unit", () -> new org.nemesis.graphic.Unit(getGame()), //
                "Projectile", () -> new org.nemesis.graphic.Projectile(getGame()) //
        );

        private final Map<String, Map<String, ? extends Reference<?>>> container = Map.of( //
                "Party", game.getParties(), //
                "Player", game.getPlayers(), //
                "Unit", game.getUnits(), //
                "Projectile", game.getProjectiles() //
        );

        @SuppressWarnings("unchecked")
        @Override
        public void update(StatusResponse reference) {
            for (Party party : reference.getPartiesList())
                ((Reference<Party>) save((Map<String, Reference<?>>) (Map<?, ?>) game.getParties(),
                        party.getSuper().getType(),
                        party.getSuper().getId())).update(party);
            for (Player player : reference.getPlayersList())
                ((Reference<Player>) save((Map<String, Reference<?>>) (Map<?, ?>) game.getPlayers(),
                        player.getSuper().getType(),
                        player.getSuper().getId())).update(player);
            for (Unit unit : reference.getUnitsList())
                ((Reference<Unit>) save((Map<String, Reference<?>>) (Map<?, ?>) game.getUnits(),
                        unit.getSuper().getSuper().getType(),
                        unit.getSuper().getSuper().getId())).update(unit);
            for (Projectile projectile : reference.getProjectilesList())
                ((Reference<Projectile>) save((Map<String, Reference<?>>) (Map<?, ?>) game.getProjectiles(),
                        projectile.getSuper().getSuper().getType(), projectile.getSuper().getSuper().getId()))
                        .update(projectile);
        }

        @Override
        public Reference<?> save(Map<String, Reference<?>> map, String type, String id) {
            Reference<?> reference = map.get(id);
            if ((reference = map.get(id)) == null) {
                Reference<?> newValue;
                if ((newValue = getCreators().get(type).create()) != null) {
                    map.put(id, newValue);
                    return newValue;
                }
            }
            return reference;
        }

        @Override
        public Map<String, Map<String, ? extends Reference<?>>> getContainer() {
            return container;
        }

        @Override
        public Map<String, Creator<? extends Reference<?>>> getCreators() {
            return creators;
        }
    };

    private final NemesisStoreHelper helper = new NemesisStoreHelper();

    private transient String token = "";

    public void status() {
        if (channel.getState(true).equals(ConnectivityState.READY))
            getHelper().update(nemesisBlockingStub.status(StatusRequest.newBuilder().setToken(token).build()));
    }

    public void change(String unitId, Point2D destiantion) {
        nemesisBlockingStub.change(ChangeRequest.newBuilder().setToken(token).setUnitId(unitId)
                .setDestination(Vector.newBuilder().setX(destiantion.getX()).setY(destiantion.getY()).build()).build());
    }

    public void join() {
        if (token.isBlank())
            token = hostingBlockingStub.join(JoinRequest.newBuilder().build()).getToken();
    }

    public long ping() {
        long now = System.currentTimeMillis();
        return hostingBlockingStub.ping(PingRequest.newBuilder().build()).getPing() - now;
    }

    public Game getGame() {
        return game;
    }

    public NemesisStoreHelper getHelper() {
        return helper;
    }
}
