package org.nemesis.game;

import javafx.geometry.Point2D;
import org.nemesis.content.FileUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;

public class SpawnPoint extends Marker {

    private final Properties properties;

    protected SpawnPoint(@Nonnull Game game, Properties properties) {
        super(game);
        this.properties = properties;
    }

    public static class Properties implements Serializable {

        @SuppressWarnings("all")
        private Point2D position;

        public Properties setPosition(Point2D position) {
            this.position = position;
            return this;
        }

        public Point2D getPosition() {
            return position;
        }

        private int number;

        public Properties setNumber(int number) {
            this.number = number;
            return this;
        }

        public int getNumber() {
            return number;
        }
    }

    private @Nullable Player player;

    @Override
    public void update() {
        if (player == null) {
            System.err.println("** spawn");
            player = new Player(getGame());
            for (int i = 0; i < 5; i++) {
                Unit unit = new Unit(player, FileUtils.getJson("unit/unit.json", Unit.Properties.class));
                unit.setPosition(getProperties().getPosition());
            }
            if (getProperties().getNumber() == 0) {
                player.markAsController();
            }
        }
        // TODO add spawn implementation
    }

    public Properties getProperties() {
        return properties;
    }
}
