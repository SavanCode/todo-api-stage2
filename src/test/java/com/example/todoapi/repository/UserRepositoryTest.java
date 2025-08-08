package com.example.todoapi.repository;

import com.example.todoapi.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("encodedPassword");
    }

    @Test
    void save_shouldPersistUser() {
        // Act
        User savedUser = userRepository.save(testUser);

        // Assert
        assertNotNull(savedUser.getId());
        assertEquals("testuser", savedUser.getUsername());
        assertEquals("test@example.com", savedUser.getEmail());
        assertNotNull(savedUser.getCreatedAt());
        assertNotNull(savedUser.getUpdatedAt());
    }

    @Test
    void findByUsername_shouldReturnUser_whenUserExists() {
        // Arrange
        User savedUser = entityManager.persistAndFlush(testUser);

        // Act
        Optional<User> foundUser = userRepository.findByUsername("testuser");

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals(savedUser.getId(), foundUser.get().getId());
        assertEquals("testuser", foundUser.get().getUsername());
    }

    @Test
    void findByUsername_shouldReturnEmpty_whenUserDoesNotExist() {
        // Act
        Optional<User> foundUser = userRepository.findByUsername("nonexistent");

        // Assert
        assertFalse(foundUser.isPresent());
    }

    @Test
    void existsByUsername_shouldReturnTrue_whenUsernameExists() {
        // Arrange
        entityManager.persistAndFlush(testUser);

        // Act
        boolean exists = userRepository.existsByUsername("testuser");

        // Assert
        assertTrue(exists);
    }

    @Test
    void existsByUsername_shouldReturnFalse_whenUsernameDoesNotExist() {
        // Act
        boolean exists = userRepository.existsByUsername("nonexistent");

        // Assert
        assertFalse(exists);
    }

    @Test
    void existsByEmail_shouldReturnTrue_whenEmailExists() {
        // Arrange
        entityManager.persistAndFlush(testUser);

        // Act
        boolean exists = userRepository.existsByEmail("test@example.com");

        // Assert
        assertTrue(exists);
    }

    @Test
    void existsByEmail_shouldReturnFalse_whenEmailDoesNotExist() {
        // Act
        boolean exists = userRepository.existsByEmail("nonexistent@example.com");

        // Assert
        assertFalse(exists);
    }

    @Test
    void findById_shouldReturnUser_whenUserExists() {
        // Arrange
        User savedUser = entityManager.persistAndFlush(testUser);

        // Act
        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals(savedUser.getId(), foundUser.get().getId());
    }

    @Test
    void findById_shouldReturnEmpty_whenUserDoesNotExist() {
        // Act
        Optional<User> foundUser = userRepository.findById(999L);

        // Assert
        assertFalse(foundUser.isPresent());
    }

    @Test
    void save_shouldUpdateExistingUser() {
        // Arrange
        User savedUser = entityManager.persistAndFlush(testUser);
        savedUser.setEmail("updated@example.com");

        // Act
        User updatedUser = userRepository.save(savedUser);

        // Assert
        assertEquals("updated@example.com", updatedUser.getEmail());
        assertEquals(savedUser.getId(), updatedUser.getId());
    }

    @Test
    void delete_shouldRemoveUser() {
        // Arrange
        User savedUser = entityManager.persistAndFlush(testUser);

        // Act
        userRepository.delete(savedUser);

        // Assert
        Optional<User> foundUser = userRepository.findById(savedUser.getId());
        assertFalse(foundUser.isPresent());
    }

    @Test
    void findAll_shouldReturnAllUsers() {
        // Arrange
        User user1 = new User();
        user1.setUsername("user1");
        user1.setEmail("user1@example.com");
        user1.setPassword("password1");

        User user2 = new User();
        user2.setUsername("user2");
        user2.setEmail("user2@example.com");
        user2.setPassword("password2");

        entityManager.persistAndFlush(user1);
        entityManager.persistAndFlush(user2);

        // Act
        var allUsers = userRepository.findAll();

        // Assert
        assertTrue(allUsers.size() >= 2);
        assertTrue(allUsers.stream().anyMatch(u -> "user1".equals(u.getUsername())));
        assertTrue(allUsers.stream().anyMatch(u -> "user2".equals(u.getUsername())));
    }
} 