package org.nemesis.menu;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.nemesis.content.ResourceManager;
import org.nemesis.game.Game;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Menu extends Scene {

    private static final @Nonnull ResourceManager manager = new ResourceManager("Menu");

    public Menu(Stage stage) {
        this(new VBox(6), stage);
    }

    private Menu(VBox root, Stage stage) {
        super(root, 400, 300);

        getStylesheets().add(ResourceManager.getStylesheet("menu.css"));

        Label title = new Label("Nemesis Snapshot");
        title.setFont(Font.font(26));
        title.setPadding(new Insets(0, 0, 10, 0));

        Label description = new Label("Choose your map");
        description.setFont(Font.font(14));

        ChoiceBox<String> choices = new ChoiceBox<>();
        choices.setConverter(new StringConverter<>() {
            @Nonnull
            @Override
            public String toString(@Nullable String key) {
                if (key == null) return "null";
                return manager.translate(key);
            }

            @Override
            public String fromString(String string) {
                throw new UnsupportedOperationException();
            }
        });
        choices.getItems().addAll(ResourceManager.getLevels());
        if (!choices.getItems().isEmpty()) choices.setValue(choices.getItems().iterator().next());

        Button button = new Button("Start new Game");
        button.setOnAction(e -> stage.setScene(new Game(new Game.GameSettings(choices.getValue()))));

        root.getChildren().addAll(title, description, choices, button);
        root.setPadding(new Insets(25));
    }
}