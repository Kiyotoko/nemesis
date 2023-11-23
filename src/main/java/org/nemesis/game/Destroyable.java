package org.nemesis.game;

import javax.annotation.WillClose;

public interface Destroyable {
    @WillClose
    void destroy();
}
