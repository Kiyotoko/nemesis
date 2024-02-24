package org.nemesis.content;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MetaLoader<T extends Identity> {

    private final Map<String, T> objects = new HashMap<>();

    public MetaLoader(String folder, Class<T> type) {
        Collection<String> collection = FileUtils.getMetaListing(folder);
        for (String item : collection) {
            T object = FileUtils.getJson(folder, item, type);
            objects.put(object.id, object);
        }
    }

    public T getObject(String id) {
        return objects.get(id);
    }
}
