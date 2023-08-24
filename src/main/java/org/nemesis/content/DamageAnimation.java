package org.nemesis.content;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.nemesis.game.Animation;
import org.nemesis.game.Projectile;

import javax.annotation.Nonnull;

public class DamageAnimation extends Animation {

    private final Circle circle = new Circle();

    public DamageAnimation(@Nonnull Projectile projectile) {
        super(projectile.getPlayer(), projectile.getPosition());
        getGraphic().getChildren().add(circle);
        Color color = projectile.getPlayer().getColor();
        circle.setStroke(color);
        circle.setFill(Color.color(color.getRed(), color.getGreen(), color.getBlue(), 0.125));
        setLiveTime(20.0);
    }

    @Override
    public void animate(double deltaT) {
        circle.setRadius(circle.getRadius() + deltaT);
    }
}
