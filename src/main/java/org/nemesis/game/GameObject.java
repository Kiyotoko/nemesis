package org.nemesis.game;

import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.annotation.WillClose;

public abstract class GameObject implements Entity {

    private final @Nonnull Game game;

    private final @Nonnull Pane pane = new Pane();

    protected GameObject(@Nonnull Game game) {
        this.game = game;

        Platform.runLater(this::relocate);
        getGame().getObjects().add(this);
        getGame().getDown().getChildren().add(getPane());
    }

    @OverridingMethodsMustInvokeSuper
    @WillClose
    public void destroy() {
        getGame().getObjects().remove(this);
        getGame().getDown().getChildren().remove(getPane());
    }

    private void relocate() {
        getPane().setLayoutX(position.getX() - getPane().getWidth() * 0.5);
        getPane().setLayoutY(position.getY() - getPane().getHeight() * 0.5);
    }

    private @Nonnull Point2D position = Point2D.ZERO;

    public void setPosition(@Nonnull Point2D position) {
        this.position = position;
        relocate();
    }

    @Nonnull
    public Point2D getPosition() {
        return position;
    }

    private double rotation;

    /**
     * @param rotation the rotation in degrees
     */
    public void setRotation(double rotation) {
        this.rotation = rotation;
        getPane().setRotate(rotation);
    }

    /**
     * @return the rotation in degrees
     */
    public double getRotation() {
        return rotation;
    }

    @Nonnull
    public Game getGame() {
        return game;
    }

    @Nonnull
    public Pane getPane() {
        return pane;
    }

}
