package org.nemesis.game;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.shape.Line;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.Deque;

public class PathAnimation extends Animation {

    private final @Nonnull ObservableList<Line> paths = FXCollections.observableArrayList();

    private final @Nonnull Unit unit;

    public PathAnimation(@Nonnull Unit unit) {
        super(unit.getGame());
        this.unit = unit;
        paths.addListener((ListChangeListener<? super Line>) change -> {
            change.next();
            if (change.wasAdded()) {
                for (Line line : change.getAddedSubList())
                    getPane().getChildren().add(line);
            }
            if (change.wasRemoved()) {
                for (Line line : change.getRemoved())
                    getPane().getChildren().remove(line);
            }
        });
        setLiveTime(Double.POSITIVE_INFINITY);
    }

    @Override
    public void animate() {
        Deque<Point2D> destinations = unit.getDestinations();
        int size = destinations.size();
        while (size > paths.size()) {
            paths.add(create());
        }
        while (size < paths.size()) {
            paths.remove(paths.size() - 1);
        }
        Point2D previous = null;
        int i = 0;
        for (Point2D destination : destinations) {
            Line path = paths.get(i);
            if (previous == null) {
                path.setStartX(unit.getPosition().getX());
                path.setStartY(unit.getPosition().getY());
            } else {
                path.setStartX(previous.getX());
                path.setStartY(previous.getY());
            }
            path.setEndX(destination.getX());
            path.setEndY(destination.getY());
            previous = destination;
            i++;
        }
    }

    @CheckReturnValue
    @Nonnull
    private Line create() {
        Line path = new Line();
        // path.setStroke(unit.getPlayer().getColor());
        return path;
    }
}
