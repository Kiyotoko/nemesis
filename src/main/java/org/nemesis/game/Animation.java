package org.nemesis.game;

import io.scvis.geometry.Vector2D;

import javax.annotation.Nonnull;

public abstract class Animation extends Physical {

    protected Animation(@Nonnull Player player, @Nonnull Vector2D position) {
        super(player, position);
        getParent().getAnimations().add(this);
    }

    @Override
    public void update(double deltaT) {
        animate(deltaT);
        count(deltaT);
    }

    public abstract void animate(double deltaT);

    public void count(double deltaT) {
        setAnimationTime(getAnimationTime() + deltaT);
    }

    @Override
    public void destroy() {
        super.destroy();
        getParent().getAnimations().remove(this);
    }

    @Override
    public void accelerate(double deltaT) {
        // No acceleration
    }

    @Override
    public void velocitate(double deltaT) {
        // No velocity
    }

    @Override
    public void displacement(double deltaT) {
        // No displacement
    }

    private double animationTime;

    public void setAnimationTime(double animationTime) {
        this.animationTime = animationTime;
        if (animationTime >= getLiveTime()) destroy();
    }

    public double getAnimationTime() {
        return animationTime;
    }

    private double liveTime;

    public void setLiveTime(double liveTime) {
        this.liveTime = liveTime;
    }

    public double getLiveTime() {
        return liveTime;
    }
}
