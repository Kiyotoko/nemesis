package org.nemesis.game;

import io.scvis.entity.Children;
import io.scvis.geometry.Vector2D;
import javafx.scene.layout.Pane;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.ArrayDeque;
import java.util.Deque;

public abstract class Physical implements Displayable, Children {

    private final @Nonnull Player player;
    private final @Nonnull Pane pane = new Pane();
    private final @Nonnull Deque<Vector2D> destinations = new ArrayDeque<>(4);

    private @Nonnull Vector2D position;
    private double speed = 1;

    protected Physical(@Nonnull Player player, @Nonnull Vector2D position) {
        this.player = player;
        this.position = position;

        getParent().getChildren().add(this);
    }

    @OverridingMethodsMustInvokeSuper
    @Override
    public void update(double deltaT) {
        displacement(deltaT);
    }

    protected abstract void displacement(double deltaT);

    @Nonnull
    @Override
    public Game getParent() {
        return getPlayer().getGame();
    }

    @Nonnull
    public Player getPlayer() {
        return player;
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
    public Vector2D getPosition() {
        return position;
    }

    public void setPosition(@Nonnull Vector2D position) {
        this.position = position;
        relocate();
    }

    @CheckReturnValue
    @Nonnull
    public Vector2D getDestination() {
        final Vector2D checked = getDestinations().peek();
        return checked != null ? checked : getPosition();
    }

    public void setDestination(@Nonnull Vector2D destination) {
        getDestinations().clear();
        getDestinations().add(destination);
    }

    @CheckReturnValue
    @Nonnull
    public Deque<Vector2D> getDestinations() {
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
