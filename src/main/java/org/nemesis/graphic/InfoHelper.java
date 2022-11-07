package org.nemesis.graphic;

import org.nemesis.App;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class InfoHelper extends Group {
    private final StringProperty name = new SimpleStringProperty();

    private final LevelBar economy = new LevelBar();
    private final LevelBar devense = new LevelBar();
    private final LevelBar military = new LevelBar();

    private String id;

    public InfoHelper(App app) {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        // ##### head #####
        BorderPane head = new BorderPane();

        Label text = new Label();
        text.textProperty().bind(name);
        text.setMnemonicParsing(true);
        text.setTextFill(Color.WHITE);
        head.setLeft(text);

        Circle hide = new Circle(12.5, Color.gray(0.8));
        hide.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            app.getUi().setCenter(null);
        });
        head.setRight(hide);

        content.getChildren().add(head);

        // ##### body #####
        HBox body = new HBox(8);

        // ##### view #####
        VBox view = new VBox(8);

        economy.getFill().set(Color.LIMEGREEN);
        military.getFill().set(Color.RED);
        devense.getFill().set(Color.CORNFLOWERBLUE);

        view.getChildren().addAll(economy, military, devense);
        body.getChildren().add(view);

        // ##### options #####
        VBox options = new VBox(8);
        options.getChildren().addAll(
                new EventButton(e -> {
                    app.getClient().changeEconomy(id, true, false);
                }), new EventButton(e -> {
                    app.getClient().changeMilitary(id, "", true, false);
                }), new EventButton(e -> {
                    app.getClient().changeDevense(id, true, false);
                }));
        body.getChildren().add(options);

        content.getChildren().add(body);

        // ##### back #####
        Rectangle back = new Rectangle();
        back.setFill(Color.gray(0.25));
        back.widthProperty().bind(content.widthProperty());
        back.heightProperty().bind(content.heightProperty());
        back.setArcHeight(50);
        back.setArcWidth(50);

        getChildren().addAll(back, content);
    }

    public StringProperty getName() {
        return name;
    }

    public LevelBar getEconomy() {
        return economy;
    }

    public LevelBar getDevense() {
        return devense;
    }

    public LevelBar getMilitary() {
        return military;
    }

    public void bindId(String value) {
        if (id == null) {
            id = value;
        }
    }
}
