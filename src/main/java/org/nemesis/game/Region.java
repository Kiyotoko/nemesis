package org.nemesis.game;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.nemesis.grpc.Corresponding;

public class Region implements Parent, Corresponding<org.nemesis.grpc.Region> {
    private final Map<String, Building> buildings = new HashMap<>();
    private final Map<String, Fleet> fleets = new HashMap<>();
    private final Set<Region> subregions = new HashSet<>();

    private Player owner = Player.UNOCCUPIED;

    private double posX;
    private double posY;

    public Region(Region center, double posX, double posY) {
        this(posX, posY);
        center.getSubregions().add(this);
    }

    public Region(double posX, double posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public Region() {

    }

    @Override
    public void update(double deltaT) {
        Parent.super.update(deltaT);
        for (Region subregion : subregions) {
            subregion.rotate(this, deltaT);
        }
    }

    public void rotate(Region center, double deltaT) {
        double difX = posX - center.posX;
        double difY = posY - center.posY;

        double radius = Math.hypot(difX, difY);
        double angel = Math.atan2(difY, difX) + (deltaT / radius * Math.PI);

        posX = center.posX + Math.cos(angel) * radius;
        posY = center.posY + Math.sin(angel) * radius;
    }

    transient String hash;

    @Override
    public String getId() {
        if (hash == null)
            hash = Integer.toHexString(hashCode());
        return hash;
    }

    @Override
    public Collection<Building> getChildren() {
        return buildings.values();
    }

    public Map<String, Building> getBuildings() {
        return buildings;
    }

    public Map<String, Fleet> getFleets() {
        return fleets;
    }

    public Set<Region> getSubregions() {
        return subregions;
    }

    public Player getPlayer() {
        return owner;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosX() {
        return posX;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public double getPosY() {
        return posY;
    }

    @Override
    public org.nemesis.grpc.Region associated() {
        return org.nemesis.grpc.Region.newBuilder().setId(getId()).setOwner(owner.getId())
                .addAllBuildings(buildings.keySet()).addAllFleets(fleets.keySet()).setPosX(posX).setPosY(posY)
                .build();
    }
}
