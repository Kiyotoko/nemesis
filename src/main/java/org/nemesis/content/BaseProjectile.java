package org.nemesis.content;

import javafx.scene.shape.Circle;
import org.nemesis.game.Projectile;
import org.nemesis.game.Unit;

import javax.annotation.Nonnull;

public class BaseProjectile extends Projectile {
    public BaseProjectile(@Nonnull Unit unit) {
        super(unit);
        getGraphic().getChildren().add(new Circle(4, unit.getPlayer().getColor()));

        setSpeed(3.2);
        setDamage(1.4);
        setCriticalChance(0.2);
        setCriticalDamage(2.0);
        setRange(150.0);
    }
}
