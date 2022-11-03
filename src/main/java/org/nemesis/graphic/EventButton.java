package org.nemesis.graphic;

import org.nemesis.App;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class EventButton extends StackPane {
    private final StringProperty name = new SimpleStringProperty("+");

    public EventButton(App app, EventHandler<MouseEvent> event) {
        Rectangle back = new Rectangle(50, 30);
        back.setFill(Color.gray(.2));
        back.setArcWidth(16);
        back.setArcHeight(16);

        Label text = new Label();
        text.textProperty().bind(name);
        text.setMnemonicParsing(true);
        text.setFont(Font.font("Ubuntu", 22));
        text.setTextFill(Color.WHITE);

        addEventHandler(MouseEvent.MOUSE_CLICKED, event);
        getChildren().addAll(back, text);
    }

    public StringProperty getName() {
        return name;
    }
}
