package org.nemesis.game;

import org.nemesis.grpc.Corresponding;

public class Player implements Entity, Corresponding<org.nemesis.grpc.Player> {

    public static final Player UNOCCUPIED = new Player() {
        @Override
        public String getId() {
            return "";
        }

        @Override
        public String toString() {
            return "unoccupied";
        }
    };

    @Override
    public void update(double deltaT) {

    }

    transient String hash;

    @Override
    public String getId() {
        if (hash == null)
            hash = Integer.toHexString(hashCode());
        return hash;
    }

    @Override
    public org.nemesis.grpc.Player associated() {
        return org.nemesis.grpc.Player.newBuilder().build();
    }

}
