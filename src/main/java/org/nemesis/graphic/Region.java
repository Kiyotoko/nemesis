package org.nemesis.graphic;

import org.nemesis.App;

import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Region extends Group implements Reference<org.nemesis.grpc.Region> {
    private final Circle region = new Circle();

    private final InfoHelper infoHelper;

    public Region(App app) {
        infoHelper = new InfoHelper(app);

        region.setFill(
                Color.rgb(80 + (int) (Math.random() * 160), 80 + (int) (Math.random() * 160),
                        80 + (int) (Math.random() * 160)));
        addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            BorderPane popups = app.getUi();
            if (popups.getCenter() == infoHelper) {
                popups.setCenter(null);
            } else {
                popups.setCenter(infoHelper);
            }
        });
        getChildren().add(region);
    }

    @Override
    public void update(org.nemesis.grpc.Region delta) {
        region.setRadius(10 + delta.getDiameter() * 10);
        region.setCenterX(delta.getPositionX());
        region.setCenterY(delta.getPositionY());

        infoHelper.getName().set(delta.getName());
        infoHelper.getEconomy().getMax().set(delta.getEconomyMaximum());
        infoHelper.getEconomy().getMin().set(delta.getEconomyDevelopment());
        infoHelper.getMilitary().getMax().set(delta.getMilitaryMaximum());
        infoHelper.getMilitary().getMin().set(delta.getMilitaryDevelopment());
        infoHelper.getDevense().getMax().set(delta.getDevenseMaximum());
        infoHelper.getDevense().getMin().set(delta.getDevenseDevelopment());
        infoHelper.bindId(delta.getId());
    }
}
