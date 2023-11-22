package org.nemesis.game;

import io.scvis.entity.Children;
import io.scvis.proto.Identifiable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import javax.annotation.CheckForNull;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Player implements Children, Identifiable, Displayable {

    private static @Nullable Player controller;
    private final @Nonnull ObservableList<Unit> units = FXCollections.observableArrayList();
    private final @Nonnull Label label = new Label(getClass().getSimpleName());
    private final @Nonnull Game game;

    private @Nonnull Color color = Color.WHITE;
    private @Nonnull String name = "Unknown";

    public Player(@Nonnull Game game) {
        this.game = game;
        label.setFont(Font.font("Ubuntu", FontWeight.BOLD, 14));
        label.setTextFill(Color.WHITE);

        ListChangeListener<Unit> listener = change -> {
            change.next();
            if (change.getList().isEmpty()) destroy();
            else label.setText(getName() + " [" + change.getList().size() + "]");
        };
        units.addListener(listener);

        getParent().getPlayers().add(this);
        getParent().getChildren().add(this);
    }

    @CheckForNull
    public static Player getController() {
        return controller;
    }

    private static void mark(int mark) {
        if (getController() != null) {
            for (Unit unit : getController().getUnits())
                if (unit.getMark() == mark) unit.setMark(Unit.UNMARKED);
            Set<Unit> selected = getController().getParent().getSelected();
            for (Unit unit : selected) {
                unit.setMark(mark);
            }
        }
    }

    private static void select(int mark) {
        if (getController() != null) {
            Set<Unit> selected = getController().getParent().getSelected();
            selected.clear();
            for (Unit unit : getController().getUnits())
                if (unit.getMark() == mark) selected.add(unit);
        }
    }

    @CheckReturnValue
    @Nonnull
    public static EventHandler<KeyEvent> getKeyEventHandler() {
        return e -> {
            if (getController() != null) {
                if (e.isControlDown()) {
                    switch (e.getCode()) {
                        case A:
                            getController().getGame().getSelected().addAll(getController().getUnits());
                            break;
                        case S:
                            getController().getParent().getSelected().forEach(unit ->
                                    unit.setDestination(unit.getPosition()));
                            break;
                        case D:
                            getController().getParent().getSelected().forEach(unit ->
                                    unit.setTarget(null));
                            break;
                        case DIGIT1:
                            mark(1);
                            break;
                        case DIGIT2:
                            mark(2);
                            break;
                        case DIGIT3:
                            mark(3);
                            break;
                        case DIGIT4:
                            mark(4);
                            break;
                        case DIGIT5:
                            mark(5);
                            break;
                        case DIGIT6:
                            mark(6);
                            break;
                        case DIGIT7:
                            mark(7);
                            break;
                        case DIGIT8:
                            mark(8);
                            break;
                        case DIGIT9:
                            mark(9);
                            break;
                        case DIGIT0:
                            mark(0);
                            break;
                        default:
                            break;
                    }
                } else {
                    switch (e.getCode()) {
                        case DIGIT1:
                            select(1);
                            break;
                        case DIGIT2:
                            select(2);
                            break;
                        case DIGIT3:
                            select(3);
                            break;
                        case DIGIT4:
                            select(4);
                            break;
                        case DIGIT5:
                            select(5);
                            break;
                        case DIGIT6:
                            select(6);
                            break;
                        case DIGIT7:
                            select(7);
                            break;
                        case DIGIT8:
                            select(8);
                            break;
                        case DIGIT9:
                            select(9);
                            break;
                        case DIGIT0:
                            select(0);
                            break;
                        default:
                            break;
                    }
                }
            }
        };
    }

    @Override
    public void update(double deltaT) {
        // Nothing to update
    }

    @Override
    public void destroy() {
        Children.super.destroy();
        getGame().getPlayers().remove(this);
        getParent().getChildren().remove(this);
    }

    public void markAsController() {
        controller = this;
    }

    @CheckReturnValue
    public boolean isController() {
        return getController() == this;
    }

    public @Nonnull Set<Field> getRenderFields() {
        Level level = getParent().getLevel();
        Set<Field> fields = new HashSet<>();
        for (Unit unit : getUnits()) {
            Field current = level.getField(unit.getPosition().getX(), unit.getPosition().getY());
            if (current == null) continue;
            double visibility = current.getSightDistance();
            for (double x = -visibility; x <= visibility; x++) {
                for (double y = -visibility; y <= visibility; y++) {
                    Field next = level.getField(unit.getPosition().getX() + x * 16, unit.getPosition().getY() + y * 16);
                    if (next != null) fields.add(next);
                }
            }
        }
        return fields;
    }

    @Nonnull
    public List<Unit> getUnits() {
        return units;
    }

    @Nonnull
    @Override
    public Game getParent() {
        return getGame();
    }

    @Nonnull
    public Game getGame() {
        return game;
    }

    @Nonnull
    @Override
    public Node getGraphic() {
        return label;
    }

    @Nonnull
    public Color getColor() {
        return color;
    }

    public void setColor(@Nonnull Color color) {
        this.color = color;
        label.setGraphic(new Circle(7, color));
    }

    @Nonnull
    public String getName() {
        return name;
    }

    public void setName(@Nonnull String name) {
        this.name = name;
        label.setText(name);
    }
}
