package org.nemesis.content;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.nemesis.game.Player;
import org.nemesis.game.Unit;

import javax.annotation.Nonnull;

public class BaseUnit extends Unit {

    private final @Nonnull Circle graphic;

    public BaseUnit(@Nonnull Player player, @Nonnull Point2D position) {
        super(player, position);
        this.graphic = new Circle(12, player.getColor());
        graphic.setStroke(Color.WHITE);
        graphic.setStrokeWidth(0);
        graphic.setCenterX(12);
        graphic.setCenterY(12);
        Circle icon = new Circle(12, player.getColor());
        icon.setStroke(Color.WHITE);
        icon.setStrokeWidth(2);

        getGraphic().getChildren().addAll(graphic);
        getIcon().getChildren().add(icon);

        setProjectileCreator(BaseProjectile::new);
        setSpeed(1.6);
        setHitPoints(12.5);
        setArmor(1.2);
        setReloadSpeed(10.0);
    }

    @Override
    public void select() {
        graphic.setStrokeWidth(2);
    }

    @Override
    public void deselect() {
        graphic.setStrokeWidth(0);
    }
}
