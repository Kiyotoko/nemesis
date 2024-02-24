package org.nemesis.content;

import com.google.gson.Gson;
import javafx.scene.image.Image;
import org.nemesis.Launcher;

import javax.annotation.Nonnull;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.net.URL;

public final class FileUtils {

    private FileUtils() {
        throw new UnsupportedOperationException();
    }

    private static final Gson gson = new Gson();

    @Nonnull
    private static InputStream getInputStream(@Nonnull String filename) {
        InputStream stream = Launcher.class.getResourceAsStream(filename);
        if (stream == null) throw new InternalError("Could not found file: " + filename);
        return stream;
    }

    @Nonnull
    public static Collection<String> getMetaListing(@Nonnull String folder) {
        try (InputStream stream = getInputStream(folder + "/index.txt")) {
            return List.of(new String(stream.readAllBytes()).split("\n"));
        } catch (IOException ex) {
            throw new InternalError("Could not close resource", ex);
        }
    }

    @Nonnull
    public static Image getArt(@Nonnull String filename) {
        return new Image(getInputStream("art/" + filename));
    }

    @Nonnull
    public static Path getCss(@Nonnull String filename) {
        try {
            URL url = Launcher.class.getResource(filename);
            if (url == null) throw new InternalError("Could not found file");
            return Path.of(url.toURI());
        } catch (URISyntaxException ex) {
            throw new InternalError("Could not format url", ex);
        }
    }

    @Nonnull
    public static <T> T getJson(@Nonnull String folder, @Nonnull String filename, @Nonnull Class<T> clazz) {
        try (InputStream stream = getInputStream(folder + "/" + filename)) {
            return gson.fromJson(new String(stream.readAllBytes()), clazz);
        } catch (IOException ex) {
            throw new InternalError("Could not close resource", ex);
        }
    }
}
