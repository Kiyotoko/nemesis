package org.nemesis.game;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nemesis.content.ImageBase;

class TestUnit {

    @Test
    void properties() {
        Unit.Properties properties = new Unit.Properties("BaseUnit").setIcon(new ImageBase("icon.png"))
                .setPane(new ImageBase("pane.png")).setArmor(1.2).setMovementSpeed(5.8).setReloadSpeed(2.0);
        Assertions.assertNotNull(properties.getPane());
    }
}
