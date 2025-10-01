package com.Assignment1.Group15.StudentAdministrationSystemManager.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StudentDashboardController {

    @GetMapping("/student/dashboard")
    public String studentDashboard(Authentication authentication, Model model) {
        if (authentication != null) {
            model.addAttribute("username", authentication.getName());
        }
        return "student/dashboard";
    }

    @GetMapping("/student/grades")
    public String studentGrades() {
        return "student/grades";
    }

    @GetMapping("/student/attendance")
    public String studentAttendance() {
        return "student/attendance";
    }
}