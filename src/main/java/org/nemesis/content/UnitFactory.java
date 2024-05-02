package org.nemesis.content;

import org.nemesis.game.Player;
import org.nemesis.game.Unit;

import javax.annotation.Nonnull;

public class UnitFactory implements Factory<Player, Unit> {

    private final @Nonnull Unit.Properties properties;

    public UnitFactory(@Nonnull Unit.Properties properties) {
        this.properties = properties;
    }

    @Override
    public Unit create(Player player) {
        return new Unit(player, properties);
    }
}
