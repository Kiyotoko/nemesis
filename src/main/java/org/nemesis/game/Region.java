package org.nemesis.game;

import java.util.Set;
import java.util.TreeSet;

import org.nemesis.grpc.Corresponding;

public class Region implements Comparable<Region>, Entity, Corresponding<org.nemesis.grpc.Region> {
    private final Game game;

    private Player owner = Player.UNOCCUPIED;
    private Region center;

    private final double size;

    private final int economyMaximum;
    private final int militaryMaximum;
    private final int devenseMaximum;

    private int economyDevelopment;
    private int militaryDevelopment;
    private int devenseDevelopment;

    private double posX, posY;

    public Region(Game game) {
        this.game = game;
        size = Math.pow(Math.random(), 2);
        economyMaximum = 1 + (int) (size * 10);
        militaryMaximum = 1 + (int) (size * 10);
        devenseMaximum = 1 + (int) (size * 10);
        game.getRegions().put(getId(), this);
    }

    public Region(Game game, double size, int economy, int military, int devense) {
        this.game = game;
        this.size = size;
        this.economyMaximum = economy;
        this.militaryMaximum = military;
        this.devenseMaximum = devense;
        game.getRegions().put(getId(), this);
    }

    static void sub(Game game, Region top) {
        Set<Region> regions = new TreeSet<>();
        for (double f = 0; f < 4; f += 1) {
            var region = new Region(game);
            region.center = top;
            regions.add(region);
        }
        double index = 1, total = regions.size();
        for (Region region : regions) {
            double angel = Math.toRadians(Math.random() * 360);
            double radius = (400.0 / region.getLayer()) *
                    (index / total);
            region.setPosX(Math.cos(angel) * radius);
            region.setPosY(Math.sin(angel) * radius);
            index++;
        }
    }

    static Region at(Game game, double x, double y) {
        var ret = new Region(game, 1, 0, 22, 0) {
            @Override
            public void update(double deltaT) {

            }
        };
        ret.setPosX(x);
        ret.setPosY(y);
        return ret;
    }

    @Override
    public void update(double deltaT) {
        rotate(deltaT);
    }

    public void rotate(double deltaT) {
        double difX = posX - center.posX;
        double difY = posY - center.posY;

        double radius = Math.hypot(difX, difY);
        double angel = Math.atan2(difY, difX) + (deltaT / (radius * 60));

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

    public Player getPlayer() {
        return owner;
    }

    public int getLayer() {
        if (center != null)
            return center.getLayer() + 1;
        return 0;
    }

    public double getSize() {
        return size;
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
                .setEconomyMaximum(economyMaximum).setEconomyDevelopment(economyDevelopment)
                .setMilitaryMaximum(militaryMaximum).setMilitaryDevelopment(militaryDevelopment)
                .setDevenseMaximum(devenseMaximum).setDevenseDevelopment(devenseDevelopment)
                .setPositionX(posX).setPositionY(posY).setDiameter(size)
                .build();
    }

    @Override
    public int compareTo(Region o) {
        return (int) Math.signum(size - o.size);
    }
}
