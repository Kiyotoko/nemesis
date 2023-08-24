package org.nemesis.content;

import io.scvis.geometry.Vector2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.nemesis.game.Player;
import org.nemesis.game.Unit;

import javax.annotation.Nonnull;

public class BaseUnit extends Unit {

    private final @Nonnull Rectangle graphic;

    public BaseUnit(@Nonnull Player player, @Nonnull Vector2D position) {
        super(player, position);
        this.graphic = new Rectangle(16, 16, player.getColor());
        graphic.setStroke(Color.BLACK);
        graphic.setStrokeWidth(2);
        Rectangle icon = new Rectangle(16, 16, player.getColor());
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
        super.select();
        graphic.setStroke(Color.WHITE);
    }

    @Override
    public void deselect() {
        super.deselect();
        graphic.setStroke(Color.BLACK);
    }
}
