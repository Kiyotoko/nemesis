package org.nemesis.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.karlz.bounds.Bounds.Polygon;
import com.karlz.bounds.Layout;
import com.karlz.bounds.Vector;
import com.karlz.entity.Children;
import com.karlz.entity.Parent;
import com.karlz.exchange.Observable;

import javafx.scene.paint.Color;

public class Player implements Parent, Children, Observable {
    private final List<Unit> units = new ArrayList<>();

    private final Map<String, Unit> controllerTargets = new HashMap<>();

    private final Party party;

    private String webColor = Color.AQUAMARINE.toString();
    private String userName = getType() + ' ' + getId();

    public Player(Party party) {
        this.party = party;
        new Unit(this, new Layout(new Polygon(List.of(new Vector(0, -20), new Vector(10, 20), new Vector(-10, 20),
                new Vector(100, 100))), Vector.ZERO, 0), 10).setDestination(new Vector(300, 300)); // TODO
        party.getParent().getPlayers().add(this);
        party.getPlayers().add(this);
    }

    @Override
    public com.karlz.grpc.game.Player associated() {
        return com.karlz.grpc.game.Player.newBuilder()
                .setSuper((com.karlz.grpc.entity.Observable) Observable.super.associated()).setWebColor(webColor)
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
