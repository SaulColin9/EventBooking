package org.booking.storage.id;

import java.util.HashMap;
import java.util.Map;

public class IncrementalIdGenerator implements IdGenerator{
    private final Map<String, Long> latestId = new HashMap<>();
    @Override
    public long getNewId(String classIdentifier) {
        latestId.putIfAbsent(classIdentifier, 0L);
        return latestId.compute(classIdentifier, (key, currId) -> currId + 1);
    }

    @Override
    public void reset() {
        latestId.clear();
    }

    @Override
    public void setInitialIds(Map<String, Long> initialIds) {
        latestId.putAll(initialIds);
    }
}
