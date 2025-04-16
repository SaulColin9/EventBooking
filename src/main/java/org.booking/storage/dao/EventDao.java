package org.booking.storage.dao;

import org.booking.model.Event;
import org.booking.storage.Storage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

public class EventDao implements Dao<Event> {
    private Storage storage;

    @Override
    public Optional<Event> get(long id) {
        return Optional.of(storage.get(id, Event.class));
    }

    @Override
    public List<Event> getAll() {
        return storage.getEntities(Event.class);
    }

    @Override
    public Event save(Event event) {
        return (Event) storage.add(event);
    }

    @Override
    public Event update(Event event) {
        return (Event) storage.update(event);
    }

    @Override
    public Optional<Event> delete(Event event) {
        return Optional.of((Event) storage.remove(event));
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }
}
