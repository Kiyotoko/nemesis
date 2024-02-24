package org.nemesis.game;

import javafx.geometry.Point2D;

import javax.annotation.Nonnull;
import java.io.Serializable;

public class SpawnPoint extends GameObject {

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

    @Override
    public void update() {
        // TODO add spawn implementation
    }

    public Properties getProperties() {
        return properties;
    }
}
