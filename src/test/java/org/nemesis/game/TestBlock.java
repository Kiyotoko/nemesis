package org.nemesis.game;

import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nemesis.content.ImageBase;

class TestBlock {
    @Test
    void properties() {
        Block.Properties properties = new Block.Properties("DefaultBlock").setPane(new ImageBase("kakaras0.png"))
                .setBlocked(false).setSightDistance(7.6);

        Assertions.assertEquals("DefaultBlock", properties.id);

        System.out.println(new Gson().toJson(properties));
    }
}
