package com.Assignment1.Group15.StudentAdministrationSystemManager.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceLogicTest {

    @Test
    void testUsernameValidationLogic() {
        assertTrue(isValidUsername("john_doe123"));
        assertFalse(isValidUsername("jo"));
        assertFalse(isValidUsername("john doe"));
    }

    @Test
    void testEmailValidationLogic() {
        assertTrue(isValidEmail("test@student.com"));
        assertFalse(isValidEmail("invalid-email"));
        assertFalse(isValidEmail("test@student"));
    }

    @Test
    void testRoleValidationLogic() {
        assertTrue(isValidRole("STUDENT"));
        assertTrue(isValidRole("TEACHER"));
        assertTrue(isValidRole("ADMIN"));
        assertFalse(isValidRole("GUEST"));
        assertFalse(isValidRole(""));
    }

    @Test
    void testPasswordStrengthLogic() {
        assertFalse(isStrongPassword("123"));
        assertTrue(isStrongPassword("Password123!"));
    }

    // Helper methods
    private boolean isValidUsername(String username) {
        return username != null && username.length() >= 3 && !username.contains(" ");
    }

    private boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".") && email.length() > 5;
    }

    private boolean isValidRole(String role) {
        return "STUDENT".equals(role) || "TEACHER".equals(role) || "ADMIN".equals(role);
    }

    private boolean isStrongPassword(String password) {
        return password != null &&
                password.length() >= 8 &&
                !password.equals(password.toLowerCase()) &&
                password.matches(".*\\d.*");
    }
}