package org.nemesis;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

class LocaleTest {
    @SuppressWarnings("all")
    @Test
    void test() throws IOException {

        Locale locale = Locale.getDefault();
        System.out.println(locale);

        Assertions.assertNotNull(getClass().getResource("locale/Menu.properties"));
        Assertions.assertNull(getClass().getResourceAsStream("locale/Menu_it.properties"));

        Assertions.assertDoesNotThrow(() -> {
            try (InputStream stream = getClass().getResourceAsStream("locale/Menu.properties")) {
                PropertyResourceBundle bundle = new PropertyResourceBundle(stream);
                Assertions.assertEquals("Test File", bundle.getString("test.txt"));
            }

            ResourceBundle bundle = ResourceBundle.getBundle("Menu", locale, getClass().getModule());
            Assertions.assertEquals("A simple test file", bundle.getString("test.txt"));
        });
    }
}
