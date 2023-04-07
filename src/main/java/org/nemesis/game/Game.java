package org.nemesis.game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.checkerframework.checker.signature.qual.Identifier;
import org.nemesis.grpc.NemesisServer.NemesisDispatchHelper;

import com.karlz.entity.Clock;
import com.karlz.entity.Parent;

public class Game implements Parent {
	private final Clock clock = new Clock(() -> {
		update(0.001);
	}, 1);

	private final Map<String, NemesisDispatchHelper> dispatchers = new HashMap<>();
	private final Map<String, Player> controllers = new HashMap<>();

	private final Set<Party> parties = new HashSet<>();
	private final Set<Player> players = new HashSet<>();
	private final Set<Unit> units = new HashSet<>();
	private final Set<Projectile> projectiles = new HashSet<>();
	private final Set<Identifier> destroyed = new HashSet<>();

	public Game() {

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

	public Set<Identifier> getDestroyed() {
		return destroyed;
	}

	@Override
	public List<Party> getChildren() {
		return getParties().stream().collect(Collectors.toList());
	}

}
