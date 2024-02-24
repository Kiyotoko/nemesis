module nemesis {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.graphics;
    requires com.google.gson;
    requires annotations;

    opens org.nemesis;
    opens org.nemesis.content;
    opens org.nemesis.game;
}