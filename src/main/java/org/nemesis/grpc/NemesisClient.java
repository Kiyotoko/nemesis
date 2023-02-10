package org.nemesis.grpc;

import java.util.Map;

import org.nemesis.graphic.Game;

import com.karlz.entity.Creator;
import com.karlz.exchange.ExchangeHelper.StoreHelper;
import com.karlz.exchange.Reference;
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

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

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

	private final Game game = new Game();

	public class NemesisStoreHelper implements StoreHelper<Reference<?>, StatusResponse> {
		private Map<String, Creator<Reference<?>>> creators = Map.of( //
				"Party", () -> new org.nemesis.graphic.Party(getGame()), //
				"Player", () -> new org.nemesis.graphic.Player(getGame()), //
				"Unit", () -> new org.nemesis.graphic.Unit(getGame()), //
				"Projectile", () -> new org.nemesis.graphic.Projectile(getGame()) //
		);

		@SuppressWarnings("unchecked")
		@Override
		public void update(StatusResponse reference) {
			for (Party party : reference.getPartiesList())
				save((Map<String, Reference<Party>>) (Map<?, ?>) game.getParties(), party.getSuper().getType(),
						party.getSuper().getId()).update(party);
			for (Player player : reference.getPlayersList())
				save((Map<String, Reference<Player>>) (Map<?, ?>) game.getPlayers(), player.getSuper().getType(),
						player.getSuper().getId()).update(player);
			for (Unit unit : reference.getUnitsList())
				save((Map<String, Reference<Unit>>) (Map<?, ?>) game.getPlayers(), unit.getSuper().getSuper().getType(),
						unit.getSuper().getSuper().getId()).update(unit);
			for (Projectile projectile : reference.getProjectilesList())
				save((Map<String, Reference<Projectile>>) (Map<?, ?>) game.getPlayers(),
						projectile.getSuper().getSuper().getType(), projectile.getSuper().getSuper().getId())
						.update(projectile);
		}

		@SuppressWarnings("unchecked")
		public <T> Reference<T> save(Map<String, Reference<T>> map, String type, String id) {
			Reference<T> reference = map.get(id);
			if ((reference = map.get(id)) == null) {
				Reference<T> newValue;
				if ((newValue = (Reference<T>) getCreators().get(type).create()) != null) {
					map.put(id, newValue);
					return newValue;
				}
			}
			return reference;
		}

		@Override
		public Reference<?> save(String type, String id) {
			return null;
		}

		@Override
		public Map<String, Creator<Reference<?>>> getCreators() {
			return creators;
		}
	};

	private final NemesisStoreHelper helper = new NemesisStoreHelper();

	private transient String token = "";

	public void status() {
		getHelper().update(nemesisBlockingStub.status(StatusRequest.newBuilder().setToken(token).build()));
	}

	public void change() {
		nemesisBlockingStub.change(ChangeRequest.newBuilder().setToken(token).build());
	}

	public void join() {
		if (token.isBlank())
			token = hostingBlockingStub.join(JoinRequest.newBuilder().build()).getToken();
		System.err.println("**JOINED GAME**");
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
