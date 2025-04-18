package org.booking.storage;

import org.booking.model.BaseEntity;
import org.booking.storage.id.IdGenerator;
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

    private IdGenerator idGenerator;

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
        Object retrievedEntity = storage.get(key);
        return retrievedEntity == null? null: clazz.cast(retrievedEntity);
    }

    @Override
    public Object add(BaseEntity t) {
        t.setId(idGenerator.getNewId(getClassIdentifier(t.getClass())));
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
        String k = getKey(t.getId(), t.getClass());
        storage.put(k, t);
        return t;
    }

    private void init(){
        if (initDataPath != null){
            logger.info("Initializing storage...");
            try {
                storage.putAll(JsonToObject.extractObjects(initDataPath));
                idGenerator.setInitialIds(getHighestIds());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Map<String, Long> getHighestIds() {
        Map<String, Long> highestIds = new HashMap<>();
        for (Map.Entry<String, Object> entry : storage.entrySet()) {
            String key = entry.getKey();
            BaseEntity entity = (BaseEntity) entry.getValue();

            String entityType = key.split(":")[0];

            long id = entity.getId();

            highestIds.merge(entityType, id, Math::max);
        }
        return highestIds;
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

    public void setIdGenerator(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }
}
