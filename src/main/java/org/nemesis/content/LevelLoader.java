package org.nemesis.content;

import com.google.gson.Gson;
import org.nemesis.game.Field;
import org.nemesis.game.Level;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public class LevelLoader {

    public static final Map<Integer, Supplier<Field>> FIELD_SUPPLIERS = Map.of(
            1, Plain::new,
            2, Water::new,
            3, Swamp::new,
            4, Mountain::new,
            5, Forest::new);

    public static class BundleSource implements Serializable {
        private final List<Integer> fields = new ArrayList<>();

        private final int width;
        private final int height;

        BundleSource(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public List<Integer> getFields() {
            return fields;
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

    private static Field buildField(int key) {
        Supplier<Field> supplier = FIELD_SUPPLIERS.get(key);
        if (supplier == null) throw new NullPointerException("No Supplier found for "+ key);
        return supplier.get();
    }

    private static Level buildBundle(BundleSource source) {
        Level build = new Level(source.getWidth(), source.getHeight());
        int x = 0;
        int y = 0;
        for (int key : source.getFields()) {
            build.setField(x, y, buildField(key));
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
