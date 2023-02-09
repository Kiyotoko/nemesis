package org.nemesis.game;

import java.util.ArrayList;
import java.util.List;

import com.karlz.entity.Children;
import com.karlz.entity.Parent;
import com.karlz.exchange.Observable;

public class Party implements Parent, Children, Observable {
	private final List<Player> players = new ArrayList<>();

	private final Game game;

	public Party(Game game) {
		this.game = game;
	}

	@Override
	public void changed() {

	}

	@Override
	public void destroy() {
		game.getParties().remove(this);
	}

	public List<Player> getPlayers() {
		return players;
	}

	@Override
	public List<Player> getChildren() {
		return players;
	}

	@Override
	public Game getParent() {
		return game;
	}
}