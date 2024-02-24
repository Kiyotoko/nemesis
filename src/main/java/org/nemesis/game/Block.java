package org.nemesis.game;

import javafx.scene.image.ImageView;
import org.nemesis.content.Identity;
import org.nemesis.content.ImageBase;

import javax.annotation.Nonnull;

public class Block {

    public static final double BLOCK_EDGE_SIZE_PX = 64;

    private final @Nonnull Properties properties;
    private final @Nonnull ImageView view;

    public Block(Properties properties) {
        this.properties = properties;
        this.view = new ImageView(properties.getPane().getImage());
    }

    public static class Properties extends Identity {
        public Properties(String id) {
            super(id);
        }

        private ImageBase pane;

        public Properties setPane(ImageBase pane) {
            this.pane = pane;
            return this;
        }

        public ImageBase getPane() {
            return pane;
        }

        private double sightDistance;

        public Properties setSightDistance(double sightDistance) {
            this.sightDistance = sightDistance;
            return this;
        }

        public double getSightDistance() {
            return sightDistance;
        }

        private boolean blocked;

        public Properties setBlocked(boolean blocked) {
            this.blocked = blocked;
            return this;
        }

        public boolean isBlocked() {
            return blocked;
        }
    }

    @Nonnull
    public Properties getProperties() {
        return properties;
    }

    @Nonnull
    public ImageView getView() {
        return view;
    }

    private boolean hidden;

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

}
