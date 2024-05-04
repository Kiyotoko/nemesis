package org.nemesis.game;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import javax.annotation.Nonnull;

public class DamageAnimation extends Animation {

    private final @Nonnull Circle circle = new Circle();

    public DamageAnimation(@Nonnull Projectile projectile) {
        super(projectile.getGame());

        setLiveTime(200);
        setPosition(projectile.getPosition());

        circle.setStroke(Color.gray(0.2));
        circle.setFill(Color.gray(0.5, 0.125));
        getPane().getChildren().add(circle);
    }

    @Override
    public void animate() {
        circle.setRadius(Math.min(circle.getRadius() + 1, 20));
    }
}
