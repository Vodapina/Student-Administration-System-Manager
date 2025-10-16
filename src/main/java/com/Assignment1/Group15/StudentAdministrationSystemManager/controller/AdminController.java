package com.Assignment1.Group15.StudentAdministrationSystemManager.controller;

import com.Assignment1.Group15.StudentAdministrationSystemManager.entity.User;
import com.Assignment1.Group15.StudentAdministrationSystemManager.entity.Student;
import com.Assignment1.Group15.StudentAdministrationSystemManager.service.UserService;
import com.Assignment1.Group15.StudentAdministrationSystemManager.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private StudentService studentService;

    // Admin Dashboard
    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        try {
            // Get user statistics
            List<User> students = userService.getAllStudents();
            List<User> teachers = userService.getAllTeachers();
            List<User> allUsers = userService.getAllUsers();

            model.addAttribute("studentCount", students.size());
            model.addAttribute("teacherCount", teachers.size());
            model.addAttribute("totalUsers", allUsers.size());

            return "admin/dashboard";

        } catch (Exception e) {
            model.addAttribute("error", "Error loading admin dashboard: " + e.getMessage());
            return "admin/dashboard";
        }
    }

    // Show all users (for management) - ENHANCED VERSION
    @GetMapping("/users")
    public String manageUsers(Model model) {
        try {
            System.out.println("=== LOADING MANAGE USERS PAGE ===");

            // Get all users
            List<User> allUsers = userService.getAllUsers();
            System.out.println("Total users found: " + allUsers.size());

            // Debug: Print each user
            for (User user : allUsers) {
                System.out.println("User: " + user.getUsername() + " | Role: " + user.getRole() + " | ID: " + user.getId());
            }

            List<User> students = userService.getAllStudents();
            List<User> teachers = userService.getAllTeachers();
            List<User> admins = userService.getAllAdmins();

            System.out.println("Students: " + students.size());
            System.out.println("Teachers: " + teachers.size());
            System.out.println("Admins: " + admins.size());

            // Get student details for students
            List<Student> studentDetails = studentService.getAllStudents();
            System.out.println("Student details found: " + studentDetails.size());

            // Debug: Print each student
            for (Student student : studentDetails) {
                System.out.println("Student: " + student.getStudentId() + " | User: " +
                        (student.getUser() != null ? student.getUser().getUsername() : "NO USER"));
            }

            model.addAttribute("users", allUsers);
            model.addAttribute("students", students);
            model.addAttribute("teachers", teachers);
            model.addAttribute("admins", admins);
            model.addAttribute("studentDetails", studentDetails);
            model.addAttribute("totalUsers", allUsers.size());

            System.out.println("=== SUCCESSFULLY LOADED USERS DATA ===");
            return "admin/manage-users";

        } catch (Exception e) {
            System.out.println("=== ERROR IN MANAGE USERS: " + e.getMessage() + " ===");
            e.printStackTrace(); // This will show the full stack trace
            model.addAttribute("error", "Error loading users: " + e.getMessage());
            return "admin/manage-users";
        }
    }

    // Edit user - show form
    @GetMapping("/users/edit/{userId}")
    public String showEditUserForm(@PathVariable Long userId, Model model) {
        try {
            User user = userService.getAllUsers().stream()
                    .filter(u -> u.getId().equals(userId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // If it's a student, get student details
            if ("STUDENT".equals(user.getRole())) {
                Student student = studentService.getStudentByUserUsername(user.getUsername())
                        .orElse(null);
                model.addAttribute("student", student);
            }

            model.addAttribute("user", user);
            return "admin/edit-user";

        } catch (Exception e) {
            model.addAttribute("error", "Error loading user: " + e.getMessage());
            return "redirect:/admin/users";
        }
    }

    // Update user
    @PostMapping("/users/update")
    public String updateUser(
            @RequestParam Long userId,
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String role,
            @RequestParam(required = false) String newPassword,
            @RequestParam(required = false) String studentId,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            Model model) {

        try {
            User user = userService.getAllUsers().stream()
                    .filter(u -> u.getId().equals(userId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Update user details
            user.setUsername(username);
            user.setEmail(email);
            user.setRole(role);

            // Update password if provided
            if (newPassword != null && !newPassword.trim().isEmpty()) {
                userService.resetPassword(userId, newPassword);
            }

            userService.updateUser(user);

            // If it's a student, update student details
            if ("STUDENT".equals(role)) {
                Student student = studentService.getStudentByUserUsername(user.getUsername())
                        .orElseThrow(() -> new RuntimeException("Student not found"));

                if (studentId != null) student.setStudentId(studentId);
                if (firstName != null) student.setFirstName(firstName);
                if (lastName != null) student.setLastName(lastName);

                studentService.updateStudent(student);
            }

            model.addAttribute("success", "User updated successfully!");
            return "redirect:/admin/users";

        } catch (Exception e) {
            model.addAttribute("error", "Error updating user: " + e.getMessage());
            return "redirect:/admin/users";
        }
    }

    // Delete user
    @PostMapping("/users/delete")
    public String deleteUser(
            @RequestParam Long userId,
            @RequestParam String role,
            Model model) {

        try {
            User user = userService.getAllUsers().stream()
                    .filter(u -> u.getId().equals(userId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // If it's a student, delete student record first
            if ("STUDENT".equals(role)) {
                Student student = studentService.getStudentByUserUsername(user.getUsername())
                        .orElse(null);
                if (student != null) {
                    studentService.deleteStudent(student.getId());
                }
            }

            // Delete user account
            boolean deleted = userService.deleteUser(userId);

            if (deleted) {
                model.addAttribute("success", "User deleted successfully!");
            } else {
                model.addAttribute("error", "Failed to delete user");
            }

            return "redirect:/admin/users";

        } catch (Exception e) {
            model.addAttribute("error", "Error deleting user: " + e.getMessage());
            return "redirect:/admin/users";
        }
    }

    // Admin settings page
    @GetMapping("/settings")
    public String adminSettings(Model model) {
        return "admin/settings";
    }
}