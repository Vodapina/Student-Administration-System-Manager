package com.Assignment1.Group15.StudentAdministrationSystemManager.controller;

import com.Assignment1.Group15.StudentAdministrationSystemManager.entity.Student;
import com.Assignment1.Group15.StudentAdministrationSystemManager.entity.Subject;
import com.Assignment1.Group15.StudentAdministrationSystemManager.entity.User;
import com.Assignment1.Group15.StudentAdministrationSystemManager.entity.Grade;
import com.Assignment1.Group15.StudentAdministrationSystemManager.service.StudentService;
import com.Assignment1.Group15.StudentAdministrationSystemManager.service.SubjectService;
import com.Assignment1.Group15.StudentAdministrationSystemManager.service.UserService;
import com.Assignment1.Group15.StudentAdministrationSystemManager.service.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private UserService userService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private GradeService gradeService;

    // === EXISTING METHODS (KEEP THESE) ===

    // Student List Page
    @GetMapping
    public String studentList(Model model) {
        List<Student> students = studentService.getAllStudents();
        model.addAttribute("students", students);
        return "students/list";
    }

    // Add Student Form
    @GetMapping("/add")
    public String showAddStudentForm(Model model) {
        model.addAttribute("student", new Student());
        return "students/add";
    }

    // Process Add Student
    @PostMapping("/add")
    public String addStudent(@RequestParam String username,
                             @RequestParam String password,
                             @RequestParam String email,
                             @RequestParam String studentId,
                             @RequestParam String firstName,
                             @RequestParam String lastName,
                             Model model) {
        try {
            // Create user first
            User user = userService.registerUser(username, password, email, "STUDENT");

            // Then create student
            Student student = studentService.createStudent(user, studentId, firstName, lastName);

            model.addAttribute("success", "Student created successfully!");
            return "redirect:/students";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "students/add";
        }
    }

    // View Student Details
    @GetMapping("/{id}")
    public String viewStudent(@PathVariable Long id, Model model) {
        Student student = studentService.getStudentById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        model.addAttribute("student", student);
        return "students/view";
    }

    // === NEW METHODS FOR STUDENT GRADE VIEWING ===

    // Student view their own grades
    @GetMapping("/my/grades")
    public String viewMyGrades(Authentication authentication, Model model) {
        try {
            String username = authentication.getName();
            System.out.println("Student viewing grades: " + username);

            // Get student by username (students login with username, not studentId)
            Student student = studentService.getStudentByUserUsername(username)
                    .orElseThrow(() -> new RuntimeException("Student not found for user: " + username));

            // Get student's Biology grades
            List<Grade> grades = gradeService.getBiologyGradesByStudent(student);

            System.out.println("Found " + grades.size() + " grades for student: " + student.getFullName());

            model.addAttribute("student", student);
            model.addAttribute("grades", grades);
            return "student/grades"; // This goes to templates/student/grades.html

        } catch (Exception e) {
            System.out.println("Error loading student grades: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Error loading grades: " + e.getMessage());
            return "student/grades";
        }
    }

    // Student view their attendance
    @GetMapping("/my/attendance")
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