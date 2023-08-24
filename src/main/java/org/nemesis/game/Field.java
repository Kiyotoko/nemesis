package org.nemesis.game;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import javax.annotation.Nonnull;

public class Field implements Displayable {

    private final Rectangle rect = new Rectangle(16,16);

    private final Color viewed;
    private final Color hidden;

    public Field(Color color) {
        this.viewed = color;
        this.hidden = color.darker().darker().darker();

        setVisible(false);
    }

    @Nonnull
    @Override
    public Node getGraphic() {
        return rect;
    }

    private boolean visible;

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
        rect.setFill(isVisible() ? viewed : hidden);
    }

    private int height = 0;

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    private double visibility = 1;

    public void setVisibility(double visibility) {
        this.visibility = visibility;
    }

    public double getVisibility() {
        return visibility;
    }

    private boolean blocked = false;

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public boolean isBlocked() {
        return blocked;
    }
}
