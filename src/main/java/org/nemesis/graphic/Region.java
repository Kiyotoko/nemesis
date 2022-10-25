package org.nemesis.graphic;

import org.nemesis.App;

import javafx.scene.Group;
import javafx.scene.shape.Circle;

public class Region extends Group implements Reference<org.nemesis.grpc.Region> {
    private final App app;

    private final Circle back = new Circle(25);

    public Region(App app) {
        this.app = app;
        getChildren().add(back);
    }

    @Override
    public void update(org.nemesis.grpc.Region delta) {
        back.setCenterX(delta.getPosX());
        back.setCenterY(delta.getPosY());
    }
}
