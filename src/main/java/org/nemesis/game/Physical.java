package org.nemesis.game;

import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.ArrayDeque;
import java.util.Deque;

public abstract class Physical implements Displayable, Entity, Destroyable {

    private final @Nonnull Player player;
    private final @Nonnull Pane pane = new Pane();
    private final @Nonnull Deque<Point2D> destinations = new ArrayDeque<>(4);

    private @Nonnull Point2D position;
    private double speed = 1;

    protected Physical(@Nonnull Player player, @Nonnull Point2D position) {
        this.player = player;
        this.position = position;

        getGame().getEntities().add(this);
    }

    @OverridingMethodsMustInvokeSuper
    @Override
    public void update() {
        displacement();
    }

    @Override
    public void destroy() {
        getGame().getEntities().remove(this);
    }

    protected abstract void displacement();

    @Nonnull
    public Player getPlayer() {
        return player;
    }

    @Nonnull
    public Game getGame() {
        return player.getGame();
    }

    @Nonnull
    @Override
    public Pane getGraphic() {
        return pane;
    }

    public void relocate() {
        pane.setLayoutX(position.getX() - pane.getWidth() / 2);
        pane.setLayoutY(position.getY() - pane.getHeight() / 2);
    }

    @CheckReturnValue
    @Nonnull
    public Point2D getPosition() {
        return position;
    }

    public void setPosition(@Nonnull Point2D position) {
        this.position = position;
        relocate();
    }

    @CheckReturnValue
    @Nonnull
    public Point2D getDestination() {
        final Point2D checked = getDestinations().peek();
        return checked != null ? checked : getPosition();
    }

    public void setDestination(@Nonnull Point2D destination) {
        getDestinations().clear();
        getDestinations().add(destination);
    }

    @CheckReturnValue
    @Nonnull
    public Deque<Point2D> getDestinations() {
        return destinations;
    }

    @CheckReturnValue
    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
}
