package org.nemesis.grpc;

public interface Creator<T> {
    public T create();
}
