package org.nemesis.game;

import javafx.scene.layout.Pane;

import javax.annotation.Nonnull;

public abstract class GameObject implements Entity {

    private final @Nonnull Game game;

    private final @Nonnull Pane pane = new Pane();
    private final @Nonnull Pane icon = new Pane();

    protected GameObject(@Nonnull Game game) {
        this.game = game;
    }

    @Nonnull
    public Game getGame() {
        return game;
    }

    @Nonnull
    public Pane getPane() {
        return pane;
    }

    @Nonnull
    public Pane getIcon() {
        return icon;
    }
}
