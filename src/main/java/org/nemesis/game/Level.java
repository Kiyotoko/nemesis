package org.nemesis.game;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

public class Level implements Displayable {

    private final @Nonnull Pane pane = new Pane();

    private final @Nonnull Field[][] fields;

    private final int columns;
    private final int rows;
    private final double width;
    private final double height;

    public Level(int columns, int rows) {
        this.columns = columns;
        this.rows = rows;
        this.width = columns * 16.0;
        this.height = rows * 16.0;

        fields = new Field[columns][rows];
    }

    public boolean isInside(double posX, double posY) {
        return (posX >= 0 && posX <= width && posY >= 0 && posY <= height);
    }

    @CheckForNull
    public Field getField(double posX, double posY) {
        if (!isInside(posX, posY)) return null;
        int x = (int) (posX / 16.0);
        int y = (int) (posY / 16.0);
        return fields[x][y];
    }

    public void setField(int x, int y, @Nonnull Field value) {
        if (x < 0 || x > columns) throw new ArrayIndexOutOfBoundsException();
        if (y < 0 || y > rows) throw new ArrayIndexOutOfBoundsException();
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

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
}
