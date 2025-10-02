package com.Assignment1.Group15.StudentAdministrationSystemManager.controller;

import com.Assignment1.Group15.StudentAdministrationSystemManager.entity.Attendance;
import com.Assignment1.Group15.StudentAdministrationSystemManager.entity.Student;
import com.Assignment1.Group15.StudentAdministrationSystemManager.service.GradeService;
import com.Assignment1.Group15.StudentAdministrationSystemManager.service.StudentService;
import com.Assignment1.Group15.StudentAdministrationSystemManager.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private GradeService gradeService;

    @Autowired
    private AttendanceService attendanceService;  // ADD THIS

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

    // === ATTENDANCE METHODS ===

    // Biology Attendance Page
    @GetMapping("/biology/attendance")
    public String biologyAttendancePage(Model model) {
        try {
            System.out.println("=== TEACHER ATTENDANCE PAGE LOADING ===");

            // Get all Form 6 students
            List<Student> biologyStudents = studentService.getStudentsByFormLevel("6");

            // Set default date to today
            LocalDate today = LocalDate.now();

            model.addAttribute("students", biologyStudents);
            model.addAttribute("selectedDate", today);
            model.addAttribute("attendance", new Attendance());

            return "teacher/biology-attendance";

        } catch (Exception e) {
            System.out.println("ERROR in biologyAttendancePage: " + e.getMessage());
            model.addAttribute("error", "Error loading students: " + e.getMessage());
            return "teacher/biology-attendance";
        }
    }

    // Process Attendance Marking
    @PostMapping("/biology/attendance/mark")
    public String markBiologyAttendance(
            @RequestParam LocalDate date,
            @RequestParam Map<String, String> allParams,
            Model model) {

        try {
            System.out.println("=== MARKING ATTENDANCE FOR DATE: " + date + " ===");

            int markedCount = 0;

            // Process each student's attendance
            for (String paramName : allParams.keySet()) {
                if (paramName.startsWith("attendance_")) {
                    Long studentId = Long.parseLong(paramName.substring(11)); // Extract student ID
                    String status = allParams.get(paramName);

                    Student student = studentService.getStudentById(studentId)
                            .orElseThrow(() -> new RuntimeException("Student not found: " + studentId));

                    attendanceService.markBiologyAttendance(student, date, status);
                    markedCount++;

                    System.out.println("Marked attendance for " + student.getFullName() + ": " + status);
                }
            }

            System.out.println("ATTENDANCE MARKED SUCCESSFULLY! Marked " + markedCount + " students.");

            return "redirect:/teacher/biology/attendance?success=true&date=" + date + "&count=" + markedCount;

        } catch (Exception e) {
            System.out.println("ERROR marking attendance: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/teacher/biology/attendance?error=" + e.getMessage();
        }
    }

    // View Attendance Records
    @GetMapping("/biology/attendance/view")
    public String viewBiologyAttendance(
            @RequestParam(required = false) LocalDate date,
            Model model) {

        try {
            LocalDate viewDate = (date != null) ? date : LocalDate.now();

            List<Attendance> attendanceRecords = attendanceService.getBiologyAttendanceByDate(viewDate);

            model.addAttribute("attendanceRecords", attendanceRecords);
            model.addAttribute("selectedDate", viewDate);
            return "teacher/view-biology-attendance";

        } catch (Exception e) {
            model.addAttribute("error", "Error loading attendance: " + e.getMessage());
            return "teacher/view-biology-attendance";
        }
    }
}