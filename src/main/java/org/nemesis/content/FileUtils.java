package org.nemesis.content;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import javafx.scene.image.Image;
import org.nemesis.Launcher;

import javax.annotation.Nonnull;
import java.io.*;

public final class FileUtils {

    private FileUtils() {
        throw new UnsupportedOperationException();
    }

    private static final Gson gson = new Gson();

    @Nonnull
    private static InputStream getResource(@Nonnull String filename) {
        InputStream stream = Launcher.class.getResourceAsStream(filename);
        if (stream == null) throw new InternalError("Could not found file: " + filename);
        return stream;
    }

    @Nonnull
    public static Image getImage(@Nonnull String filename) {
        return new Image(getResource(filename));
    }

    @Nonnull
    public static <T> T getJson(@Nonnull String path, @Nonnull Class<T> type) {
        try (InputStream stream = getResource(path)) {
            return gson.fromJson(new String(stream.readAllBytes()), type);
        } catch (JsonSyntaxException ex) {
            throw new InternalError("Json syntax error", ex);
        } catch (IOException ex) {
            throw new InternalError("IO error occurred", ex);
        }
    }
}
