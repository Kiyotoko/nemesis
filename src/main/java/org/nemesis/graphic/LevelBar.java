package org.nemesis.graphic;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Group;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class LevelBar extends Group {
    private final ObjectProperty<Color> fill = new SimpleObjectProperty<>();
    private final IntegerProperty min = new SimpleIntegerProperty();
    private final IntegerProperty max = new SimpleIntegerProperty();

    public LevelBar() {
        Rectangle back = new Rectangle(240, 30);
        back.setFill(Color.gray(.2));
        back.setArcWidth(16);
        back.setArcHeight(16);

        GridPane content = new GridPane();
        content.setLayoutX(4);
        content.setLayoutY(4);

        DoubleProperty division = new SimpleDoubleProperty();
        division.bind(max);

        Rectangle action = new Rectangle(232, 22);
        action.fillProperty().bind(fill);
        action.widthProperty().bind(min.divide(division).multiply(232));
        action.setArcWidth(16);
        action.setArcHeight(16);

        Text label = new Text();
        label.textProperty().bind(new SimpleStringProperty(" ").concat(min).concat(" / ").concat(max));
        label.setFont(Font.font("Ubuntu", 16));
        label.setFill(Color.WHITE);

        content.getChildren().addAll(action, label);

        getChildren().addAll(back, content);
    }

    public ObjectProperty<Color> getFill() {
        return fill;
    }

    public IntegerProperty getMin() {
        return min;
    }

    public IntegerProperty getMax() {
        return max;
    }
}
