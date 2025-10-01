package com.Assignment1.Group15.StudentAdministrationSystemManager.controller;

import com.Assignment1.Group15.StudentAdministrationSystemManager.entity.Student;
import com.Assignment1.Group15.StudentAdministrationSystemManager.service.GradeService;
import com.Assignment1.Group15.StudentAdministrationSystemManager.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private GradeService gradeService;

    // Biology Grade Entry Page
    @GetMapping("/biology/grades")
    public String biologyGradesPage(Model model) {
        try {
            System.out.println("=== TEACHER GRADES PAGE LOADING ===");

            // DEBUG: Check if studentService is working
            System.out.println("StudentService: " + (studentService != null ? "OK" : "NULL"));

            // Get all students
            List<Student> students = studentService.getAllStudents();
            System.out.println("Found " + students.size() + " students");

            // DEBUG: Print each student
            for (Student student : students) {
                System.out.println("Student: " + student.getFullName() + " ID: " + student.getId());
            }

            model.addAttribute("students", students);
            return "teacher/biology-grades";

        } catch (Exception e) {
            System.out.println("ERROR in biologyGradesPage: " + e.getMessage());
            e.printStackTrace(); // THIS WILL SHOW THE FULL STACK TRACE
            model.addAttribute("error", "Error loading students: " + e.getMessage());
            return "teacher/biology-grades";
        }
    }

    // FIXED: Now using your enterBiologyGrade method!
    @PostMapping("/biology/grades/enter")
    public String enterBiologyGrade(
            @RequestParam Long studentId,
            @RequestParam String grade,
            @RequestParam String term,
            Model model) {

        try {
            System.out.println("ENTERING GRADE - Student: " + studentId + ", Grade: " + grade + ", Term: " + term);

            // Get the student
            Student student = studentService.getStudentById(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));

            // USE YOUR EXISTING METHOD - this handles Biology subject automatically!
            gradeService.enterBiologyGrade(student, grade, term);

            System.out.println("GRADE SAVED SUCCESSFULLY!");

            // Success message will be shown after redirect
            return "redirect:/teacher/biology/grades?success=true&student=" + student.getFullName() + "&grade=" + grade;

        } catch (Exception e) {
            System.out.println("ERROR entering grade: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/teacher/biology/grades?error=" + e.getMessage();
        }
    }

    // View Biology Grades
    @GetMapping("/biology/grades/view")
    public String viewBiologyGrades(Model model) {
        try {
            var grades = gradeService.getAllBiologyGrades();
            model.addAttribute("grades", grades);
            return "teacher/view-biology-grades";
        } catch (Exception e) {
            model.addAttribute("error", "Error loading grades: " + e.getMessage());
            return "teacher/view-biology-grades";
        }
    }
}