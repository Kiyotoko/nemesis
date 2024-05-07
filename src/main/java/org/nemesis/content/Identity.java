package org.nemesis.content;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.io.Serializable;

@Immutable
public class Identity implements Serializable {
    public final String id;

    public Identity(@Nonnull String id) {
        this.id = id;
    }

    public void withContentLoader(@Nonnull ContentLoader loader) {
        // For override
    }
}
