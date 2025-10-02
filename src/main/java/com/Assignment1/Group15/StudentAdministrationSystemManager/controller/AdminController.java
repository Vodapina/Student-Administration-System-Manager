package com.Assignment1.Group15.StudentAdministrationSystemManager.controller;

import com.Assignment1.Group15.StudentAdministrationSystemManager.entity.User;
import com.Assignment1.Group15.StudentAdministrationSystemManager.entity.Student;
import com.Assignment1.Group15.StudentAdministrationSystemManager.service.UserService;
import com.Assignment1.Group15.StudentAdministrationSystemManager.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    // Show all users (for management)
    @GetMapping("/users")
    public String viewAllUsers(Model model) {
        try {
            List<User> allUsers = userService.getAllUsers();
            List<User> students = userService.getAllStudents();
            List<User> teachers = userService.getAllTeachers();
            List<User> admins = userService.getAllAdmins();

            model.addAttribute("allUsers", allUsers);
            model.addAttribute("students", students);
            model.addAttribute("teachers", teachers);
            model.addAttribute("admins", admins);

            return "admin/users";

        } catch (Exception e) {
            model.addAttribute("error", "Error loading users: " + e.getMessage());
            return "admin/users";
        }
    }

    // Admin settings page
    @GetMapping("/settings")
    public String adminSettings(Model model) {
        return "admin/settings";
    }
}