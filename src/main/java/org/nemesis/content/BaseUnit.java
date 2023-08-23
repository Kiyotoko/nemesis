package org.nemesis.content;

import io.scvis.geometry.Vector2D;
import javafx.scene.shape.Rectangle;
import org.nemesis.game.Player;
import org.nemesis.game.Unit;

import javax.annotation.Nonnull;

public class BaseUnit extends Unit {

    public BaseUnit(@Nonnull Player player, @Nonnull Vector2D position) {
        super(player, position);
        getGraphic().getChildren().add(new Rectangle(16, 16, player.getColor()));
        getIcon().getChildren().add(new Rectangle(16, 16, player.getColor()));

        setProjectileCreator(BaseProjectile::new);
        setSpeed(1.6);
        setHitPoints(12.5);
        setArmor(1.2);
        setReloadSpeed(10.0);
    }
}
