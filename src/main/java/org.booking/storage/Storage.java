package org.booking.storage;

import org.booking.model.BaseEntity;

import java.util.List;

public interface Storage {
    <T> List<T> getEntities(Class<T> clazz);
    <T> T get(long id, Class<T> clazz);
    Object add(BaseEntity t);
    Object remove(BaseEntity t);
    Object update(BaseEntity t);
}
