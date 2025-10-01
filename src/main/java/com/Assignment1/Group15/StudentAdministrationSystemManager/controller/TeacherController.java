package com.Assignment1.Group15.StudentAdministrationSystemManager.controller;

import com.Assignment1.Group15.StudentAdministrationSystemManager.entity.Grade;
import com.Assignment1.Group15.StudentAdministrationSystemManager.entity.Student;
import com.Assignment1.Group15.StudentAdministrationSystemManager.entity.Subject;
import com.Assignment1.Group15.StudentAdministrationSystemManager.service.GradeService;
import com.Assignment1.Group15.StudentAdministrationSystemManager.service.StudentService;
import com.Assignment1.Group15.StudentAdministrationSystemManager.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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

    @Autowired
    private SubjectService subjectService;

    // Biology Grade Entry Page
    @GetMapping("/biology/grades")
    public String biologyGradesPage(Model model) {
        // Get all Form 6 students (assuming they're all Biology students for demo)
        List<Student> biologyStudents = studentService.getStudentsByFormLevel("6");

        // Get Biology subject
        Subject biology = subjectService.getSubjectByCode("BIO6")
                .orElseThrow(() -> new RuntimeException("Biology subject not found"));

        model.addAttribute("students", biologyStudents);
        model.addAttribute("subject", biology);
        model.addAttribute("grade", new Grade());

        return "teacher/biology-grades";
    }

    // Process Grade Entry
    @PostMapping("/biology/grades/enter")
    public String enterBiologyGrade(
            @RequestParam Long studentId,
            @RequestParam String grade,
            @RequestParam String term,
            Model model) {

        try {
            Student student = studentService.getStudentById(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            gradeService.enterBiologyGrade(student, grade, term);

            model.addAttribute("success", "Grade entered successfully for " + student.getFullName());
        } catch (Exception e) {
            model.addAttribute("error", "Error entering grade: " + e.getMessage());
        }

        return "redirect:/teacher/biology/grades";
    }

    // View All Biology Grades
    @GetMapping("/biology/grades/view")
    public String viewBiologyGrades(Model model) {
        List<Grade> biologyGrades = gradeService.getAllBiologyGrades();
        model.addAttribute("grades", biologyGrades);
        return "teacher/view-biology-grades";
    }
}