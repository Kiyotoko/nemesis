package org.nemesis.game;

import javax.annotation.Nonnull;

public abstract class Animation extends GameObject implements Destroyable {

    protected Animation(@Nonnull Game game) {
        super(game);
        getGame().getAnimations().add(this);
    }

    @Override
    public void update() {
        animate();
        count();
    }

    public abstract void animate();

    public void count() {
        setAnimationTime(getAnimationTime() + 1);
    }

    @Override
    public void destroy() {
        getGame().getAnimations().remove(this);
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
