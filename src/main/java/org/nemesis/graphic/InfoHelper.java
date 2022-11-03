package org.nemesis.graphic;

import org.nemesis.App;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
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

    private final DoubleProperty economy = new SimpleDoubleProperty();
    private final DoubleProperty devense = new SimpleDoubleProperty();
    private final DoubleProperty military = new SimpleDoubleProperty();

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

        LevelBar ec = new LevelBar(app);
        economy.addListener((observable, o, n) -> {
            if (n.doubleValue() == 0) {
                ec.getFill().set(Color.gray(.5));
                ec.getPercentage().set(1);
            } else {
                ec.getPercentage().set(n.doubleValue());
            }
        });
        ec.getFill().set(Color.LIMEGREEN);

        LevelBar de = new LevelBar(app);
        devense.addListener((observable, o, n) -> {
            if (n.doubleValue() == 0) {
                de.getFill().set(Color.gray(.5));
                de.getPercentage().set(1);
            } else {
                de.getPercentage().set(n.doubleValue());
            }
        });
        de.getFill().set(Color.CORNFLOWERBLUE);

        LevelBar mi = new LevelBar(app);
        military.addListener((observable, o, n) -> {
            if (n.doubleValue() == 0) {
                mi.getFill().set(Color.gray(.5));
                mi.getPercentage().set(1);
            } else {
                mi.getPercentage().set(n.doubleValue());
            }
        });
        mi.getFill().set(Color.RED);

        view.getChildren().addAll(ec, de, mi);
        body.getChildren().add(view);

        // ##### options #####
        VBox options = new VBox(8);
        options.getChildren().addAll(new EventButton(app, e -> {
        }), new EventButton(app, e -> {
        }), new EventButton(app, e -> {
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

    public DoubleProperty getEconomy() {
        return economy;
    }

    public DoubleProperty getDevense() {
        return devense;
    }

    public DoubleProperty getMilitary() {
        return military;
    }
}
