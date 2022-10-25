package org.nemesis.game;

import org.nemesis.grpc.Corresponding;

public class Building implements Entity, Corresponding<org.nemesis.grpc.Building> {

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
    public org.nemesis.grpc.Building associated() {
        return org.nemesis.grpc.Building.newBuilder().build();
    }

}
