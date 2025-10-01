package com.Assignment1.Group15.StudentAdministrationSystemManager.controller;

import com.Assignment1.Group15.StudentAdministrationSystemManager.entity.Student;
import com.Assignment1.Group15.StudentAdministrationSystemManager.entity.Grade;
import com.Assignment1.Group15.StudentAdministrationSystemManager.service.StudentService;
import com.Assignment1.Group15.StudentAdministrationSystemManager.service.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/student")  // NOTE: singular "student" for self-service
public class StudentPortalController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private GradeService gradeService;

    // Student view their own grades
    @GetMapping("/mygrades")  // URL: /student/mygrades
    public String viewMyGrades(Authentication authentication, Model model) {
        try {
            String username = authentication.getName();
            System.out.println("=== STUDENT GRADE VIEW ===");
            System.out.println("Logged in username: " + username);

            // Get student by username
            Student student = studentService.getStudentByUserUsername(username)
                    .orElseThrow(() -> new RuntimeException("Student not found for user: " + username));

            System.out.println("Student found: " + student.getFullName());

            // Get student's Biology grades
            List<Grade> grades = gradeService.getBiologyGradesByStudent(student);
            System.out.println("Found " + grades.size() + " grades for student");

            model.addAttribute("student", student);
            model.addAttribute("grades", grades);
            return "student/grades"; // This goes to templates/student/grades.html

        } catch (Exception e) {
            System.out.println("ERROR loading student grades: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Error loading grades: " + e.getMessage());
            return "student/grades";
        }
    }

    // Student view their attendance
    @GetMapping("/myattendance")  // URL: /student/myattendance
    public String viewMyAttendance(Authentication authentication, Model model) {
        try {
            String username = authentication.getName();
            Student student = studentService.getStudentByUserUsername(username)
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            model.addAttribute("student", student);
            model.addAttribute("message", "Attendance records will be available soon!");
            return "student/attendance";

        } catch (Exception e) {
            model.addAttribute("error", "Error loading attendance: " + e.getMessage());
            return "student/attendance";
        }
    }
}