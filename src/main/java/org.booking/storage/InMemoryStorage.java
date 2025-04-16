package org.booking.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.booking.model.BaseEntity;
import org.springframework.context.annotation.PropertySource;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InMemoryStorage implements Storage {
    private final Map<String, Object> storage = new HashMap<>();

    private String initDataPath;

    @Override
    public <T> List<T> getEntities(Class<T> clazz){
        return storage.keySet().stream()
                .filter(k-> k.contains(clazz.getSimpleName().toLowerCase()))
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
    public Object remove(BaseEntity t) {
        return storage.remove(getKey(t.getId(), t.getClass()));
    }

    @Override
    public Object update(BaseEntity t) {
        return storage.put(getKey(t.getId(), t.getClass()), t);
    }

    private void init(){
        try {
            storage.putAll(JsonToObject.extractObjects(initDataPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> String getKey(long id, Class<T> clazz) {
        return String.format("%s:%s", clazz.getSimpleName().toLowerCase(), id);
    }

    public void setInitDataPath(String initDataPath) {
        this.initDataPath = initDataPath;
    }
}
