package com.Assignment1.Group15.StudentAdministrationSystemManager.repository;

import com.Assignment1.Group15.StudentAdministrationSystemManager.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testSaveAndFindUser() {
        // This is the simplest test - just save and find
        User user = new User("testuser", "password123", "test@student.com", "STUDENT");

        User savedUser = userRepository.save(user);
        assertNotNull(savedUser.getId());

        Optional<User> foundUser = userRepository.findById(savedUser.getId());
        assertTrue(foundUser.isPresent());
        assertEquals("testuser", foundUser.get().getUsername());
    }

    @Test
    void testFindByUsername() {
        User user = new User("john_doe", "password", "john@student.com", "STUDENT");
        userRepository.save(user);

        Optional<User> found = userRepository.findByUsername("john_doe");
        assertTrue(found.isPresent());
        assertEquals("john@student.com", found.get().getEmail());
    }
}