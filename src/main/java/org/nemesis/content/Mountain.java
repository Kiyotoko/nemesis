package org.nemesis.content;

import javafx.scene.paint.Color;
import org.nemesis.game.Field;

public class Mountain extends Field {

    public Mountain() {
        super(Color.INDIANRED);
        setBlocked(true);
    }
}
