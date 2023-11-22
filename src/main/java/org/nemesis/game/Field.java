package org.nemesis.game;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import javax.annotation.Nonnull;

public class Field implements Displayable {

    private final @Nonnull Rectangle rect = new Rectangle(16,16);

    private final @Nonnull Color colorViewed;
    private final @Nonnull Color colorHidden;

    public Field(@Nonnull Color color) {
        this.colorViewed = color;
        this.colorHidden = color.darker().darker().darker();

        setHidden(false);
    }

    @Nonnull
    @Override
    public Node getGraphic() {
        return rect;
    }

    private boolean hidden;

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
        rect.setFill(isHidden() ? colorViewed : colorHidden);
    }

    private double sightDistance = 1;

    public void setSightDistance(double sightDistance) {
        this.sightDistance = sightDistance;
    }

    public double getSightDistance() {
        return sightDistance;
    }

    private boolean blocked = false;

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public boolean isBlocked() {
        return blocked;
    }
}
