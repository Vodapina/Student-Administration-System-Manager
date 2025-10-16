package com.Assignment1.Group15.StudentAdministrationSystemManager.controller;

import com.Assignment1.Group15.StudentAdministrationSystemManager.entity.User;
import com.Assignment1.Group15.StudentAdministrationSystemManager.entity.Student;
import com.Assignment1.Group15.StudentAdministrationSystemManager.service.UserService;
import com.Assignment1.Group15.StudentAdministrationSystemManager.service.StudentService;
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

    // ===== PUBLIC REGISTRATION (First Admin Only) =====
    @GetMapping("/register")
    public String showRegistrationForm() {
        System.out.println("=== REGISTRATION FORM ACCESSED ===");
        return "register";
    }

    @PostMapping("/admin/register-user")
    public String registerUserAsAdmin(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String email,
            @RequestParam String role,
            @RequestParam(required = false) String studentId,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            Model model) {

        try {
            // Register the user
            User user = userService.registerUser(username, password, email, role);

            // If registering a student, create student profile
            if ("STUDENT".equals(role)) {
                String generatedStudentId = studentId != null && !studentId.trim().isEmpty()
                        ? studentId
                        : "S" + String.format("%07d", user.getId());

                // Create student with first and last name
                Student student = studentService.createStudent(user, generatedStudentId, firstName, lastName);
            }

            model.addAttribute("success", "User registered successfully: " + username + " (" + role + ")");
            return "admin/register-user";

        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "admin/register-user";
        }
    }

    // ===== ADMIN PORTAL =====
    @GetMapping("/admin/portal")
    public String adminPortal(Model model) {
        System.out.println("=== ADMIN PORTAL ACCESSED ===");

        try {
            boolean adminExists = userService.doesAdminExist();
            System.out.println("Admin exists result: " + adminExists);

            model.addAttribute("allowRegistration", !adminExists);
            model.addAttribute("message", adminExists ?
                    "Login with administrator credentials." :
                    "No administrator account found. Please create the first admin account.");

        } catch (Exception e) {
            System.out.println("ERROR in adminPortal: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("allowRegistration", true);
            model.addAttribute("message", "System setup required");
        }

        return "admin/portal";
    }

    @GetMapping("/admin/register-user")
    public String showAdminUserRegistrationForm(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return "redirect:/login";
        }
        return "admin/register-user";
    }

    // ===== LOGIN PAGES =====
    @GetMapping("/login")
    public String showLogin() {
        System.out.println("=== UNIVERSAL LOGIN PAGE ===");
        return "login";
    }
}