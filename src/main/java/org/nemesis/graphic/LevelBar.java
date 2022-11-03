package org.nemesis.graphic;

import org.nemesis.App;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class LevelBar extends Group {
    private final ObjectProperty<Color> fill = new SimpleObjectProperty<>();
    private final DoubleProperty percentage = new SimpleDoubleProperty();

    public LevelBar(App app) {
        Rectangle back = new Rectangle(240, 30);
        back.setFill(Color.gray(.2));
        back.setArcWidth(16);
        back.setArcHeight(16);

        Rectangle action = new Rectangle(232, 22);
        action.fillProperty().bind(fill);
        action.widthProperty().bind(percentage.multiply(232));
        action.setLayoutX(4);
        action.setLayoutY(4);
        action.setArcWidth(16);
        action.setArcHeight(16);

        getChildren().addAll(back, action);
    }

    public ObjectProperty<Color> getFill() {
        return fill;
    }

    public DoubleProperty getPercentage() {
        return percentage;
    }
}
