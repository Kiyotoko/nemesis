package org.nemesis.content;

import javafx.scene.paint.Color;
import org.nemesis.game.Field;

public class Water extends Field {

    public Water() {
        super(Color.CORNFLOWERBLUE);
        setBlocked(true);
    }
}
