package org.nemesis.game;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import javax.annotation.Nonnull;

public class DamageAnimation extends Animation {

    private final @Nonnull Circle circle = new Circle();

    public DamageAnimation(@Nonnull Projectile projectile) {
        super(projectile.getGame());

        getPane().getChildren().add(circle);
        Color color = projectile.getPlayer().getColor();
        circle.setStroke(color);
        circle.setFill(Color.color(color.getRed(), color.getGreen(), color.getBlue(), 0.125));
        circle.setCenterX(projectile.getPosition().getX());
        circle.setCenterY(projectile.getPosition().getY());
        setLiveTime(20.0);
    }

    @Override
    public void animate() {
        circle.setRadius(circle.getRadius() + 1);
    }
}
