package org.nemesis.game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.nemesis.grpc.NemesisServer.NemesisDispatchHelper;

import com.karlz.bounds.World;
import com.karlz.entity.Clock;
import com.karlz.exchange.Observable;

public class Game extends World {
	private final Clock clock = new Clock(() -> {
		update(1);
	}, 5l);

	private final Map<String, NemesisDispatchHelper> dispatchers = new HashMap<>();
	private final Map<String, Player> controllers = new HashMap<>();

	private final Set<Party> parties = new HashSet<>();
	private final Set<Player> players = new HashSet<>();
	private final Set<Unit> units = new HashSet<>();
	private final Set<Projectile> projectiles = new HashSet<>();
	private final Set<Observable> destroyed = new HashSet<>();

	public Game(double width, double height) {
		super(width, height);
	}

	public Clock getClock() {
		return clock;
	}

	public Map<String, NemesisDispatchHelper> getDispatchers() {
		return dispatchers;
	}

	public Map<String, Player> getControllers() {
		return controllers;
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
