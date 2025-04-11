package org.booking.storage;

import org.booking.model.BaseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InMemoryStorage implements Storage {
    private final Map<String, Object> storage = new HashMap<>();

    @Override
    public  <T> List<T> getEntities(Class<T> clazz){
        return storage.keySet().stream()
                .filter(k-> k.contains(clazz.getName().toLowerCase()))
                .map(k->clazz.cast(storage.get(k)))
                .collect(Collectors.toList());
    }

    @Override
    public <T> T get(long id, Class<T> clazz) {
        String key = getKey(id, clazz);
        return clazz.cast(storage.get(key));
    }

    @Override
    public Object add(BaseEntity t) {
        String key = getKey(t.getId(), t.getClass());
        return storage.put(key, t);
    }

    @Override
    public void remove(BaseEntity t) {
        storage.remove(getKey(t.getId(), t.getClass()));
    }

    @Override
    public void update(BaseEntity t) {
        storage.put(getKey(t.getId(), t.getClass()), t);
    }

    private <T> String getKey(long id, Class<T> clazz) {
        return String.format("%s:%s", clazz.getName().toLowerCase(), id);
    }
}
