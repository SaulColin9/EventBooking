package org.booking.storage.dao;

import org.booking.model.BaseEntity;

import java.util.List;
import java.util.Optional;

public interface Dao<T extends BaseEntity> {
    Optional<T> get(long id);
    List<T> getAll();
    T save(T t);
    T update(T t);
    Optional<T> delete(T t);
}