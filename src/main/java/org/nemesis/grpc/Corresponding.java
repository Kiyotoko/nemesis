package org.nemesis.grpc;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

public interface Corresponding<T> {
    T associated();

    public static <T> Collection<T> transform(Collection<? extends Corresponding<T>> collection) {
        Vector<T> transformed = new Vector<>(collection.size());
        int index = 0;
        for (Iterator<? extends Corresponding<T>> iterator = collection.iterator(); iterator.hasNext(); index++) {
            transformed.add(index, iterator.next().associated());
        }
        return transformed;
    }
}
