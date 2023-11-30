package org.nemesis;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DisplacementTest {

    @Test
    void angles() {
        Assertions.assertEquals(1, Math.cos(0));
        Assertions.assertEquals(0, Math.sin(0));
    }

    @Test
    void atan() {
        Assertions.assertEquals(0,Math.atan2(0, 1));
        Assertions.assertEquals(Math.PI / 2, Math.atan2(1, 0));
    }
}
