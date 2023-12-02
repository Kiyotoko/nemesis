package org.nemesis.content;

import org.nemesis.Launcher;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

public class ResourceManager {

    private static final Class<?> loader = Launcher.class;

    public static byte[] getFileContent(@Nonnull String filename) {
        try (InputStream stream = loader.getResourceAsStream(filename)) {
            if (stream == null) throw new InternalError(filename, new NullPointerException());
            return stream.readAllBytes();
        } catch (IOException e) {
            throw new InternalError(filename, e);
        }
    }

    private static final @Nonnull Set<String> levels = Set.of(new String(getFileContent("level/levels.txt")).split("\n"));

    @Nonnull
    public static Set<String> getLevels() {
        return levels;
    }

    public static String getLevel(@Nonnull String filename) {
        return new String(getFileContent("level/" + filename));
    }

    public static String getStylesheet(@Nonnull String filename) {
        URL path = loader.getResource("style/" + filename);
        if (path == null) throw new InternalError();
        return path.toExternalForm();
    }

    private final ResourceBundle bundle;

    public ResourceManager(String basename) {

        Locale locale = Locale.getDefault();
        bundle = ResourceBundle.getBundle(basename, locale);
    }

    @Nonnull
    public String translate(@Nonnull String key) {
        return bundle.getString(key);
    }
}
