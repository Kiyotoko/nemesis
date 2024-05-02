package org.nemesis.content;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import java.io.Serializable;

public class ImageBase implements Serializable {
    private final String image;
    private int scaleX;
    private int scaleY;

    public ImageBase(String image) {
        this.image = image;
    }

    private Image upscale(Image image) {
        WritableImage writable = new WritableImage((int) image.getWidth() * scaleX,
                (int) image.getHeight() * scaleY);

        int endX = (int) image.getWidth();
        int endY = (int) image.getHeight();

        PixelReader reader = image.getPixelReader();
        PixelWriter writer = writable.getPixelWriter();

        for (int y = 0; y < endY; y++) {
            for (int x = 0; x < endX; x++) {
                int argb = reader.getArgb(x, y);
                for (int wy = 0; wy < scaleX; wy++) {
                    for (int wx = 0; wx < scaleY; wx++) {
                        writer.setArgb(x * scaleX + wx, y * scaleX + wy, argb);
                    }
                }
            }
        }

        return writable;
    }

    public ImageBase setScaleX(int scaleX) {
        this.scaleX = scaleX;
        return this;
    }

    public ImageBase setScaleY(int scaleY) {
        this.scaleY = scaleY;
        return this;
    }

    private transient Image loaded;

    public Image getImage() {
        if (loaded == null) {
            loaded = FileUtils.getImage(image);
            if (scaleX != 1 || scaleY != 1) loaded = upscale(loaded);
        }
        return loaded;
    }
}
