package com.Assignment1.Group15.StudentAdministrationSystemManager.service;

import com.Assignment1.Group15.StudentAdministrationSystemManager.entity.User;
import com.Assignment1.Group15.StudentAdministrationSystemManager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(String username, String password, String email, String role) {
        // Check if user already exists
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }

        // Create new user
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password)); // Encrypt password
        user.setEmail(email);
        user.setRole(role.toUpperCase()); // STUDENT, TEACHER, ADMIN

        return userRepository.save(user);
    }
    // In UserService.java
    public void resetPassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    // Simple version - just save the user
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElse(null);
    }

    // NEW METHOD: Check if any admin user exists - KEEP ONLY THIS VERSION
    public boolean doesAdminExist() {
        try {
            System.out.println("=== CHECKING IF ADMIN EXISTS ===");
            boolean exists = userRepository.existsByRole("ADMIN");
            System.out.println("Admin exists: " + exists);
            return exists;
        } catch (Exception e) {
            System.out.println("ERROR in doesAdminExist: " + e.getMessage());
            e.printStackTrace();
            return false; // Return false if there's an error
        }
    }

    // NEW METHOD: Get all users by role
    public List<User> getUsersByRole(String role) {
        return userRepository.findByRole(role.toUpperCase());
    }

    // NEW METHOD: Get all students
    public List<User> getAllStudents() {
        return getUsersByRole("STUDENT");
    }

    // NEW METHOD: Get all teachers
    public List<User> getAllTeachers() {
        return getUsersByRole("TEACHER");
    }

    // NEW METHOD: Get all admins
    public List<User> getAllAdmins() {
        return getUsersByRole("ADMIN");
    }

    // NEW METHOD: Delete user by ID (admin only)
    public boolean deleteUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            userRepository.delete(user.get());
            return true;
        }
        return false;
    }

    // NEW METHOD: Check if username exists
    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    // NEW METHOD: Check if email exists
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }
}