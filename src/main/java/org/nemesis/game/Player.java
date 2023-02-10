package org.nemesis.game;

import java.util.ArrayList;
import java.util.List;

import com.google.type.Color;
import com.karlz.bounds.Bounds.Circle;
import com.karlz.bounds.Layout;
import com.karlz.bounds.Vector;
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
		new Unit(this, new Layout(new Circle(Vector.ZERO, 20)), 20).setTarget(new Vector(40, 40)); // TODO
		party.getParent().getPlayers().add(this);
		party.getPlayers().add(this);
	}

	@Override
	public com.karlz.grpc.game.Player associated() {
		return com.karlz.grpc.game.Player.newBuilder()
				.setSuper((com.karlz.grpc.entity.Observable) Observable.super.associated()).build();
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
