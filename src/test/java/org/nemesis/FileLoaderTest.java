package org.nemesis;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

class FileLoaderTest {
    @Test
    void load() {
        Assertions.assertDoesNotThrow( ()-> {
            System.out.println(getClass().getResource("level/test.txt"));
            try (InputStream stream = getClass().getResourceAsStream("level/test.txt")) {
                System.out.println(stream != null ? new String(stream.readAllBytes()) : "null");
            }
        });
    }
}
