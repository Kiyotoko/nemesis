package org.nemesis.game;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class BarIndicator extends Pane implements Entity {
    private final Rectangle back = new Rectangle();
    private final Rectangle fill = new Rectangle();

    public BarIndicator(double width, Paint paint) {
        back.setFill(Color.TRANSPARENT);
        back.setWidth(width);
        back.setHeight(5);
        back.setStrokeWidth(1);
        back.setStroke(Color.BLACK);
        fill.setFill(paint);
        fill.setWidth(width);
        fill.setHeight(5);

        setVisible(false);
        getChildren().addAll(back, fill);
    }

    private double value;

    @Override
    public void update() {
        fill.setWidth(value * back.getWidth());
    }

    public void setValue(double value) {
        this.value = value;
        update();
    }
}
