package org.nemesis.content;

import java.io.Serializable;

public class Identity implements Serializable {
    public final String id;

    public Identity(String id) {
        this.id = id;
    }
}
