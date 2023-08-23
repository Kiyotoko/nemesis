package org.nemesis.game;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import javax.annotation.Nonnull;

public class Level implements Displayable {

    private static final @Nonnull Field VOID = new Field(Color.TRANSPARENT);
    static  {
        VOID.setBlocked(true);
        VOID.setHeight(Integer.MAX_VALUE);
    }

    private final @Nonnull Pane pane = new Pane();

    private final @Nonnull Field[][] fields;

    private final int width;
    private final int height;
    private final double levelWidth;
    private final double levelHeight;

    public Level(int width, int height) {
        this.width = width;
        this.height = height;
        this.levelWidth = width * 16.0;
        this.levelHeight = height * 16.0;

        fields = new Field[width][height];
    }

    @Nonnull
    public Field getField(double posX, double posY) {
        if (posX < 0 || posX > levelWidth) return VOID;
        if (posY < 0 || posY > levelHeight) return VOID;
        int x = (int) (posX / 16.0);
        int y = (int) (posY / 16.0);
        return fields[x][y];
    }

    public void setField(int x, int y, @Nonnull Field value) {
        if (x < 0 || x > width) throw new ArrayIndexOutOfBoundsException();
        if (y < 0 || y > height) throw new ArrayIndexOutOfBoundsException();
        fields[x][y] = value;
        pane.getChildren().add(value.getGraphic());
        value.getGraphic().setLayoutX(x * 16.0);
        value.getGraphic().setLayoutY(y * 16.0);
    }

    @Nonnull
    @Override
    public Node getGraphic() {
        return pane;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
