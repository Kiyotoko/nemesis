package org.nemesis.game;

import io.scvis.entity.Children;
import io.scvis.entity.Kinetic;
import io.scvis.geometry.Vector2D;
import javafx.scene.layout.Pane;

import javax.annotation.Nonnull;

public abstract class Physical implements Displayable, Kinetic, Children {

    private final @Nonnull Player player;

    private final @Nonnull Pane pane = new Pane();

    protected Physical(@Nonnull Player player, @Nonnull Vector2D position) {
        this.player = player;
        this.position = position;

        getParent().getChildren().add(this);
    }

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

    @Nonnull
    private Vector2D position;

    public void setPosition(@Nonnull Vector2D position) {
        this.position = position;
        relocate();
    }

    @Nonnull
    public Vector2D getPosition() {
        return position;
    }

    @Nonnull
    private Vector2D destination = Vector2D.ZERO;

    public void setDestination(@Nonnull Vector2D destination) {
        this.destination = destination;
    }

    @Nonnull
    public Vector2D getDestination() {
        return destination;
    }

    private double speed = 1;

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
}
