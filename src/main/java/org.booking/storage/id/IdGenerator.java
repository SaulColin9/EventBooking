package org.booking.storage.id;

import java.util.Map;

public interface IdGenerator {
    long getNewId(String classIdentifier);
    void reset();
    void setInitialIds(Map<String, Long> initialIds);
}
