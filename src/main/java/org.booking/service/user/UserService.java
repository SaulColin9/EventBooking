package org.booking.service.user;

import org.booking.model.User;

import java.util.List;

public interface UserService {
    User getUserById(long userId);
    User getUserByEmail(String email);
    List<User> getUsersByName(String name, int pageSize, int pageNum);
    User createUser(User user);
    User updateUser(User user);
    boolean deleteUser(long userId);
}
