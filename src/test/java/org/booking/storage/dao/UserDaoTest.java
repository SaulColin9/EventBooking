package org.booking.storage.dao;

import org.booking.model.User;
import org.booking.model.UserImpl;
import org.booking.storage.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UserDaoTest {

    private UserDao userDao;
    private Storage storage;

    @BeforeEach
    void setUp() {
        storage = mock(Storage.class);
        userDao = new UserDao();
        userDao.setStorage(storage);
    }

    @Test
    void testGetById() {
        User mockUser = new UserImpl();
        mockUser.setId(1L);
        mockUser.setName("Alice");
        when(storage.get(1L, User.class)).thenReturn(mockUser);

        Optional<User> result = userDao.get(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        assertThat(result.get().getName()).isEqualTo("Alice");

        verify(storage, times(1)).get(1L, User.class);
    }

    @Test
    void testGetByIdWhenNotFound() {
        when(storage.get(5L, User.class)).thenReturn(null);

        Optional<User> result = userDao.get(5L);

        assertThat(result).isEmpty();

        verify(storage, times(1)).get(5L, User.class);
    }

    @Test
    void testGetAll() {
        User user1 = new UserImpl();
        user1.setId(1L);
        user1.setName("Alice");
        User user2 = new UserImpl();
        user2.setId(2L);
        user2.setName("Bob");
        List<User> mockUsers = Arrays.asList(user1, user2);
        when(storage.getEntities(User.class)).thenReturn(mockUsers);

        List<User> result = userDao.getAll();

        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(user1, user2);

        verify(storage, times(1)).getEntities(User.class);
    }

    @Test
    void testSave() {
        User userToSave = new UserImpl();
        userToSave.setName("Alice");
        userToSave.setEmail("alice@example.com");

        User storedUser = new UserImpl();
        storedUser.setId(1L);
        storedUser.setName(userToSave.getName());
        storedUser.setEmail(userToSave.getEmail());
        when(storage.add(userToSave)).thenReturn(storedUser);

        User result = userDao.save(userToSave);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Alice");
        assertThat(result.getEmail()).isEqualTo("alice@example.com");

        verify(storage, times(1)).add(userToSave);
    }

    @Test
    void testUpdate() {
        User existingUser = new UserImpl();
        existingUser.setId(1L);
        existingUser.setName("Alice");
        existingUser.setEmail("alice@example.com");

        User updatedUser = new UserImpl();
        updatedUser.setId(1L);
        updatedUser.setName("Alice Updated");
        updatedUser.setEmail("alice.new@example.com");

        when(storage.update(existingUser)).thenReturn(updatedUser);

        User result = userDao.update(existingUser);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Alice Updated");
        assertThat(result.getEmail()).isEqualTo("alice.new@example.com");

        verify(storage, times(1)).update(existingUser);
    }

    @Test
    void testDelete() {
        User mockUser = new UserImpl();
        mockUser.setId(1L);
        when(storage.remove(mockUser)).thenReturn(mockUser);

        Optional<User> result = userDao.delete(mockUser);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);

        verify(storage, times(1)).remove(mockUser);
    }

    @Test
    void testDeleteWhenNotFound() {
        User mockUser = new UserImpl();
        mockUser.setId(1L);
        when(storage.remove(mockUser)).thenReturn(null);

        Optional<User> result = userDao.delete(mockUser);

        assertThat(result).isEmpty();

        verify(storage, times(1)).remove(mockUser);
    }
}