package org.nemesis.game;

import javafx.geometry.Point2D;

public interface Kinetic {
    void displacement();

    void setPosition(Point2D position);

    Point2D getPosition();
}
