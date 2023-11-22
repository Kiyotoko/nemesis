package org.nemesis.content;

import com.google.gson.Gson;
import javafx.scene.paint.Color;
import org.nemesis.game.Field;
import org.nemesis.game.Level;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.function.Supplier;

public class LevelLoader {

    public static class FieldSource implements Serializable, Supplier<Field> {

        private final Color color;
        private final double sightDistance;
        private final boolean blocked;

        FieldSource(Color color, double sightDistance, boolean blocked) {
            this.color = color;
            this.sightDistance = sightDistance;
            this.blocked = blocked;
        }

        @Override
        public Field get() {
            Field build = new Field(getColor());
            build.setSightDistance(getSightDistance());
            build.setBlocked(isBlocked());
            return build;
        }

        public Color getColor() {
            return color;
        }

        public double getSightDistance() {
            return sightDistance;
        }

        public boolean isBlocked() {
            return blocked;
        }
    }

    public static class BundleSource implements Serializable {
        private final List<Integer> fields = new ArrayList<>();
        private final Map<Integer, FieldSource> types = new HashMap<>();

        private final int width;
        private final int height;

        BundleSource(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public List<Integer> getFields() {
            return fields;
        }

        public Map<Integer, FieldSource> getTypes() {
            return types;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }

    private static final Gson gson = new Gson();

    private final Level loaded;

    public LevelLoader(File file) {
        try (FileReader reader = new FileReader(file)) {
            LevelLoader.BundleSource bundle = gson.fromJson(reader, LevelLoader.BundleSource.class);
            Objects.requireNonNull(bundle);
            loaded = buildBundle(bundle);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Level buildBundle(BundleSource source) {
        Level build = new Level(source.getWidth(), source.getHeight());
        int x = 0;
        int y = 0;
        for (int key : source.getFields()) {
            build.setField(x, y, source.getTypes().get(key).get());
            x++;
            if (x >= source.getWidth()) {
                x = 0;
                y++;
            }
        }
        return build;
    }

    public Level getLoaded() {
        return loaded;
    }
}
