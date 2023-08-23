package org.nemesis.content;

import javafx.scene.shape.Rectangle;
import org.nemesis.game.Player;
import org.nemesis.game.Unit;

import javax.annotation.Nonnull;

public class BaseUnit extends Unit {

    public BaseUnit(@Nonnull Player player) {
        super(player);
        getGraphic().getChildren().add(new Rectangle(16, 16, player.getColor()));
        getIcon().getChildren().add(new Rectangle(16, 16, player.getColor()));

        setSpeed(2.4);
        setHitPoints(2.4);
        setArmor(1.2);
    }
}
