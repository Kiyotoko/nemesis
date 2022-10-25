package org.nemesis.game;

import org.nemesis.grpc.Corresponding;

public class Fleet implements Entity, Corresponding<org.nemesis.grpc.Fleet> {

    @Override
    public void update(double deltaT) {

    }

    transient String hash;
    
    @Override
    public String getId() {
        if(hash == null) hash = Integer.toHexString(hashCode());
        return hash;
    }

    @Override
    public org.nemesis.grpc.Fleet associated() {
        return org.nemesis.grpc.Fleet.newBuilder().build();
    }
}
