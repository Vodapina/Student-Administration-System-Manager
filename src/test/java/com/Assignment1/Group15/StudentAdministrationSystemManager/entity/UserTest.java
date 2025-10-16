package com.Assignment1.Group15.StudentAdministrationSystemManager.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserCreationWithConstructor() {
        User user = new User("testuser", "password123", "test@student.com", "STUDENT");

        assertEquals("testuser", user.getUsername());
        assertEquals("password123", user.getPassword());
        assertEquals("test@student.com", user.getEmail());
        assertEquals("STUDENT", user.getRole());
        assertNotNull(user.getCreatedAt());
    }

    @Test
    void testUserCreationWithSetters() {
        User user = new User();
        user.setId(1L);
        user.setUsername("john_doe");
        user.setPassword("secret");
        user.setEmail("john@school.com");
        user.setRole("TEACHER");

        assertEquals(1L, user.getId());
        assertEquals("john_doe", user.getUsername());
        assertEquals("secret", user.getPassword());
        assertEquals("john@school.com", user.getEmail());
        assertEquals("TEACHER", user.getRole());
    }

    @Test
    void testRoleConstants() {
        assertEquals("STUDENT", User.ROLE_STUDENT);
        assertEquals("TEACHER", User.ROLE_TEACHER);
        assertEquals("ADMIN", User.ROLE_ADMIN);
    }

    @Test
    void testRoleCheckingMethods() {
        User student = new User();
        student.setRole(User.ROLE_STUDENT);

        User teacher = new User();
        teacher.setRole(User.ROLE_TEACHER);

        User admin = new User();
        admin.setRole(User.ROLE_ADMIN);

        assertTrue(student.isStudent());
        assertFalse(student.isTeacher());
        assertFalse(student.isAdmin());

        assertTrue(teacher.isTeacher());
        assertFalse(teacher.isStudent());
        assertFalse(teacher.isAdmin());

        assertTrue(admin.isAdmin());
        assertFalse(admin.isStudent());
        assertFalse(admin.isTeacher());
    }
}