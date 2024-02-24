package org.nemesis.game;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nemesis.content.ImageBase;

class TestProjectile {
    @Test
    void properties() {
        Projectile.Properties properties = new Projectile.Properties("BaseProjectile").setIcon(new ImageBase("icon.png"))
                .setPane(new ImageBase("pane.png")).setDamage(10.0).setCriticalChance(0.2).setCriticalDamage(2.4)
                .setMovementSpeed(20.4);

        Assertions.assertNotNull(properties.getIcon());
    }
}
