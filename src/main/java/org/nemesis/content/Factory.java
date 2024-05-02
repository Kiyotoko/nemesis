package org.nemesis.content;

public interface Factory<I, O> {
    O create(I input);
}
