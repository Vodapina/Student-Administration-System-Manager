package com.Assignment1.Group15.StudentAdministrationSystemManager.controller;

import com.Assignment1.Group15.StudentAdministrationSystemManager.entity.Student;
import com.Assignment1.Group15.StudentAdministrationSystemManager.entity.Subject;
import com.Assignment1.Group15.StudentAdministrationSystemManager.entity.User;
import com.Assignment1.Group15.StudentAdministrationSystemManager.service.StudentService;
import com.Assignment1.Group15.StudentAdministrationSystemManager.service.SubjectService;
import com.Assignment1.Group15.StudentAdministrationSystemManager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
}