package org.nemesis.game;

import java.util.ArrayList;
import java.util.List;

import com.google.type.Color;
import com.karlz.entity.Children;
import com.karlz.entity.Parent;
import com.karlz.exchange.Observable;

public class Player implements Parent, Children, Observable {
	private final List<Unit> units = new ArrayList<>();

	private final Party party;
	private final Color color;

	public Player(Party party, Color color) {
		this.party = party;
		this.color = color;
		party.getPlayers().add(this);
	}

	@Override
	public void changed() {

	}

	public List<Unit> getUnits() {
		return units;
	}

	public Party getParty() {
		return party;
	}

	public Color getColor() {
		return color;
	}

	@Override
	public Party getParent() {
		return party;
	}

	@Override
	public List<Unit> getChildren() {
		return units;
	}
}
