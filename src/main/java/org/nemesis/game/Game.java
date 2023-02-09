package org.nemesis.game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.karlz.bounds.World;
import com.karlz.exchange.Corresponding;
import com.karlz.exchange.ExchangeHelper.DispatchHelper;
import com.karlz.exchange.Observable;
import com.karlz.grpc.game.StatusResponse;

public class Game extends World {
	private final Map<String, DispatchHelper<Corresponding<?>, StatusResponse>> dispatcher = new HashMap<>();

	private final Set<Party> parties = new HashSet<>();
	private final Set<Player> players = new HashSet<>();
	private final Set<Unit> units = new HashSet<>();
	private final Set<Projectile> projectiles = new HashSet<>();
	private final Set<Observable> destroyed = new HashSet<>();

	public Game(double width, double height) {
		super(width, height);
	}

	public Map<String, DispatchHelper<Corresponding<?>, StatusResponse>> getDispatcher() {
		return dispatcher;
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
