package org.booking.storage.dao;

import org.booking.model.User;
import org.booking.storage.Storage;

import java.util.List;
import java.util.Optional;

public class UserDao implements Dao<User> {
    private Storage storage;

    @Override
    public Optional<User> get(long id) {
        return Optional.ofNullable(storage.get(id, User.class));
    }

    @Override
    public List<User> getAll() {
        return storage.getEntities(User.class);
    }

    @Override
    public User save(User user) {
        return (User) storage.add(user);
    }

    @Override
    public User update(User user) {
        return (User) storage.update(user);
    }

    @Override
    public Optional<User> delete(User user) {
        return Optional.of((User) storage.remove(user));
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }
}
