package com.Assignment1.Group15.StudentAdministrationSystemManager.controller;

import com.Assignment1.Group15.StudentAdministrationSystemManager.entity.User;
import com.Assignment1.Group15.StudentAdministrationSystemManager.entity.Student;
import com.Assignment1.Group15.StudentAdministrationSystemManager.entity.Subject;
import com.Assignment1.Group15.StudentAdministrationSystemManager.service.UserService;
import com.Assignment1.Group15.StudentAdministrationSystemManager.service.StudentService;
import com.Assignment1.Group15.StudentAdministrationSystemManager.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private SubjectService subjectService;

    // Show public registration form - ONLY FOR ADMIN REGISTRATION
    // Show public registration form - SIMPLE VERSION
    @GetMapping("/register")
    public String showRegistrationForm() {
        System.out.println("=== REGISTRATION FORM ACCESSED ===");
        return "register";
    }
    // Process public registration
    @PostMapping("/register")
    public String registerUser(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String email,
            @RequestParam String role,
            Model model) {

        System.out.println("=== REGISTRATION ATTEMPT ===");
        System.out.println("Username: " + username);
        System.out.println("Email: " + email);
        System.out.println("Role: " + role);

        try {
            // Check if admin already exists
            boolean adminExists = userService.doesAdminExist();

            if (adminExists) {
                model.addAttribute("error", "Registration is disabled. Administrator account already exists.");
                return "register";
            }

            // Only allow ADMIN role
            if (!"ADMIN".equals(role)) {
                model.addAttribute("error", "Only administrator registration is allowed.");
                return "register";
            }

            // Register the admin user
            User user = userService.registerUser(username, password, email, role);

            System.out.println("Registration SUCCESS: " + user.getUsername());
            model.addAttribute("success", "Administrator account created successfully! You can now login.");
            return "register";

        } catch (Exception e) {
            System.out.println("REGISTRATION ERROR: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "register";
        }
    }

    // Admin Portal Entry Point - DEBUG VERSION
    @GetMapping("/admin/portal")
    public String adminPortal(Model model) {
        System.out.println("=== ADMIN PORTAL ACCESSED ===");

        try {
            // Test the UserService method
            boolean adminExists = userService.doesAdminExist();
            System.out.println("Admin exists result: " + adminExists);

            model.addAttribute("allowRegistration", !adminExists);
            model.addAttribute("message", adminExists ?
                    "Login with administrator credentials." :
                    "No administrator account found. Please create the first admin account.");

        } catch (Exception e) {
            System.out.println("ERROR in adminPortal: " + e.getMessage());
            e.printStackTrace();
            // Fallback values
            model.addAttribute("allowRegistration", true);
            model.addAttribute("message", "System setup required");
        }

        return "admin/portal";
    }
    // ADMIN-ONLY: Show user registration form (for creating students/teachers)
    @GetMapping("/admin/register-user")
    public String showAdminUserRegistrationForm(Model model) {
        // Check if user is authenticated and is admin
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return "redirect:/login";
        }

        // In a real app, you'd check roles properly via UserService
        // For now, we'll assume this endpoint is secured via SecurityConfig
        return "admin/register-user";
    }

    // ADMIN-ONLY: Process user registration (for creating students/teachers)
    @PostMapping("/admin/register-user")
    public String registerUserAsAdmin(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String email,
            @RequestParam String role,
            @RequestParam(required = false) String studentId,
            @RequestParam(required = false) String fullName,
            Model model) {

        try {
            // Register the user
            User user = userService.registerUser(username, password, email, role);

            // If registering a student, create student profile
            if ("STUDENT".equals(role)) {
                String generatedStudentId = studentId != null && !studentId.trim().isEmpty()
                        ? studentId
                        : "F6BIO" + String.format("%03d", user.getId());

                String studentFullName = fullName != null && !fullName.trim().isEmpty()
                        ? fullName
                        : username;

                // FIXED: Correct method call with 4 parameters
                Student student = studentService.createStudent(user, generatedStudentId, studentFullName, "6");
            }

            model.addAttribute("success", "User registered successfully: " + username + " (" + role + ")");
            return "admin/register-user";

        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "admin/register-user";
        }
    }

    // Show login form
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }
}