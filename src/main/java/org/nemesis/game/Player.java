package org.nemesis.game;

import javax.annotation.CheckForNull;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class Player {

    private static @Nullable Player controller;

    private final @Nonnull Game game;
    private final @Nonnull List<Unit> units = new ArrayList<>();

    public Player(@Nonnull Game game) {
        this.game = game;

        getGame().getPlayers().add(this);
    }

    @CheckForNull
    public static Player getController() {
        return controller;
    }

    @SuppressWarnings("all")
    public void markAsController() {
        controller = this;
    }

    @CheckReturnValue
    public boolean isController() {
        return getController() == this;
    }

    @Nonnull
    public List<Unit> getUnits() {
        return units;
    }

    @Nonnull
    public Game getGame() {
        return game;
    }
}
