package org.booking.service.user;

import org.booking.model.User;
import org.booking.storage.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{
    private UserDao userDao;
    
    @Override
    public User getUserById(long userId) {
        return userDao.get(userId).orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public User getUserByEmail(String email) {
        return userDao.getAll().stream()
                .filter(user -> user.getEmail().equals(email))
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        list -> {
                            if (list.size() != 1) {
                                throw new IllegalStateException();
                            }
                            return list.get(0);
                        }
                ));
    }

    @Override
    public List<User> getUsersByName(String name, int pageSize, int pageNum) {
        return userDao.getAll().stream()
                .filter(user -> user.getName().equals(name))
                .skip((long) (pageNum - 1) * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());
    }

    @Override
    public User createUser(User user) {
        return userDao.save(user);
    }

    @Override
    public User updateUser(User user) {
        return userDao.update(user);
    }

    @Override
    public boolean deleteUser(long userId) {
        User userToDelete = userDao.get(userId).orElseThrow(IllegalArgumentException::new);
        return userDao.delete(userToDelete).isPresent();
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

}
