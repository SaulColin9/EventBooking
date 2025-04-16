package org.booking.storage;

import org.booking.model.BaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InMemoryStorage implements Storage {
    private final Map<String, Object> storage = new HashMap<>();
    private final static Logger logger = LoggerFactory.getLogger(InMemoryStorage.class);

    private String initDataPath;

    @Override
    public <T> List<T> getEntities(Class<T> clazz){
        return storage.keySet().stream()
                .filter(k-> k.contains(getClassIdentifier(clazz)))
                .map(k-> clazz.cast(storage.get(k)))
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
        storage.put(key, t);
        return t;
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
        logger.info("Initializing storage...");
        try {
            storage.putAll(JsonToObject.extractObjects(initDataPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> String getKey(long id, Class<T> clazz) {
        return String.format("%s:%s", getClassIdentifier(clazz), id);
    }

    private <T> String getClassIdentifier(Class<T> clazz){
        String className = clazz.getSimpleName();
        String camelCase = Character.toLowerCase(className.charAt(0)) + className.substring(1);

        return getFirstWord(camelCase);
    }

    private String getFirstWord(String camelCaseString) {
        if (camelCaseString == null || camelCaseString.isEmpty()) {
            return "";
        }

        StringBuilder firstWord = new StringBuilder();
        for (int i = 0; i < camelCaseString.length(); i++) {
            char c = camelCaseString.charAt(i);
            if (Character.isUpperCase(c)) {
                break;
            }
            firstWord.append(c);
        }

        return firstWord.toString();
    }


    public void setInitDataPath(String initDataPath) {
        this.initDataPath = initDataPath;
    }
}
