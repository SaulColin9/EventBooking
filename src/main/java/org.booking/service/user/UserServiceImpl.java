package org.booking.service.user;

import org.booking.model.User;
import org.booking.service.event.EventServiceImpl;
import org.booking.storage.dao.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{
    private UserDao userDao;
    private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public User getUserById(long userId) {
        logger.info("Getting user with id of {}", userId);
        return userDao.get(userId).orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public User getUserByEmail(String email) {
        logger.info("Getting user with email {}", email);
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
        logger.info("Getting users with name of {}", name);
        return userDao.getAll().stream()
                .filter(user -> user.getName().equals(name))
                .skip((long) (pageNum - 1) * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());
    }

    @Override
    public User createUser(User user) {
        logger.info("Creating user");
        return userDao.save(user);
    }

    @Override
    public User updateUser(User user) {
        logger.info("Updating user");
        return userDao.update(user);
    }

    @Override
    public boolean deleteUser(long userId) {
        logger.info("Deleting user with id {}", userId);
        User userToDelete = userDao.get(userId).orElseThrow(IllegalArgumentException::new);
        return userDao.delete(userToDelete).isPresent();
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

}
