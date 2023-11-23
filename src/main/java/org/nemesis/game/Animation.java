package org.nemesis.game;

import javafx.geometry.Point2D;

import javax.annotation.Nonnull;

public abstract class Animation extends Physical {

    protected Animation(@Nonnull Player player, @Nonnull Point2D position) {
        super(player, position);
        getGame().getAnimations().add(this);
    }

    @Override
    public void update() {
        super.update();
        animate();
        count();
    }

    public abstract void animate();

    public void count() {
        setAnimationTime(getAnimationTime() + 1);
    }

    @Override
    public void destroy() {
        super.destroy();
        getGame().getAnimations().remove(this);
    }

    @Override
    public void displacement() {
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
