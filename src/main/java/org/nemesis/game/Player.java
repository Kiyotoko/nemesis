package org.nemesis.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nemesis.grpc.Factory.UnitFactory;

import com.karlz.entity.Children;
import com.karlz.entity.Parent;
import com.karlz.exchange.Identifiable;

import javafx.scene.paint.Color;

public class Player implements Parent, Children, Identifiable {
	private final List<Unit> units = new ArrayList<>();

	private final Map<String, Unit> controllerTargets = new HashMap<>();

	private final Party party;

	private String webColor = Color.AQUAMARINE.toString();
	private String userName = getType() + ' ' + getId();

	public Player(Party party) {
		this.party = party;
		UnitFactory.INFANTERY.apply(this).place(100, 200);
		UnitFactory.ARTILLERY.apply(this).place(200, 200);
		UnitFactory.AIRCRAFT.apply(this).place(300, 200);
		UnitFactory.TANK.apply(this).place(400, 200);
		party.getPlayers().add(this);
	}

	@Override
	public com.karlz.grpc.game.Player associated() {
		return com.karlz.grpc.game.Player.newBuilder()
				.setSuper((com.karlz.grpc.exchange.Identifiable) Identifiable.super.associated()).setWebColor(webColor)
				.setUserName(userName).build();
	}

	public List<Unit> getUnits() {
		return units;
	}

	public Map<String, Unit> getControllerTargets() {
		return controllerTargets;
	}

	public Party getParty() {
		return party;
	}

	@Override
	public Party getParent() {
		return party;
	}

	@Override
	public List<Unit> getChildren() {
		return units;
	}

	public String getWebColor() {
		return webColor;
	}

	public void setWebColor(String webColor) {
		if (webColor != null && !webColor.isBlank())
			this.webColor = webColor;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		if (userName != null && !userName.isBlank())
			this.userName = userName;
	}
}
