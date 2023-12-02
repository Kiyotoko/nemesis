package org.nemesis.content;

import com.google.gson.Gson;
import javafx.scene.paint.Color;
import org.nemesis.game.Field;
import org.nemesis.game.Level;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.*;
import java.util.*;
import java.util.function.Supplier;

public class LevelLoader {

    public static class FieldSource implements Serializable, Supplier<Field> {

        private final @Nonnull String color;
        private final double sightDistance;
        private final boolean blocked;

        FieldSource(@Nonnull String color, double sightDistance, boolean blocked) {
            this.color = color;
            this.sightDistance = sightDistance;
            this.blocked = blocked;
        }

        private transient @Nullable Color loaded;

        @CheckReturnValue
        @Nonnull
        @Override
        public Field get() {
            Field build = new Field(getColor());
            build.setSightDistance(getSightDistance());
            build.setBlocked(isBlocked());
            return build;
        }

        @CheckReturnValue
        @Nonnull
        public Color getColor() {
            if (loaded == null) loaded = Color.web(color);
            return loaded;
        }

        @CheckReturnValue
        public double getSightDistance() {
            return sightDistance;
        }

        @CheckReturnValue
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

    public LevelLoader(String file) {
        LevelLoader.BundleSource bundle = gson.fromJson(ResourceManager.getLevel(file), LevelLoader.BundleSource.class);
        Objects.requireNonNull(bundle);
        loaded = buildBundle(bundle);
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
