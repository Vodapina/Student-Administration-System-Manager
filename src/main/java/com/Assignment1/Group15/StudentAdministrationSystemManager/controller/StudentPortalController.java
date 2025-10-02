package com.Assignment1.Group15.StudentAdministrationSystemManager.controller;

import com.Assignment1.Group15.StudentAdministrationSystemManager.entity.Attendance;
import com.Assignment1.Group15.StudentAdministrationSystemManager.entity.Student;
import com.Assignment1.Group15.StudentAdministrationSystemManager.entity.Grade;
import com.Assignment1.Group15.StudentAdministrationSystemManager.service.StudentService;
import com.Assignment1.Group15.StudentAdministrationSystemManager.service.GradeService;
import com.Assignment1.Group15.StudentAdministrationSystemManager.service.AttendanceService; // ADD THIS IMPORT
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentPortalController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private GradeService gradeService;

    @Autowired
    private AttendanceService attendanceService; // ADD THIS DEPENDENCY

    // Student view their own grades
    @GetMapping("/mygrades")
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
            return "student/grades";

        } catch (Exception e) {
            System.out.println("ERROR loading student grades: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Error loading grades: " + e.getMessage());
            return "student/grades";
        }
    }

    // Student view their attendance records
    @GetMapping("/myattendance")
    public String viewMyAttendance(Authentication authentication, Model model) {
        try {
            String username = authentication.getName();
            System.out.println("=== STUDENT VIEWING ATTENDANCE ===");
            System.out.println("Student: " + username);

            // Get student by username
            Student student = studentService.getStudentByUserUsername(username)
                    .orElseThrow(() -> new RuntimeException("Student not found for user: " + username));

            // Get student's Biology attendance records
            List<Attendance> attendanceRecords = attendanceService.getBiologyAttendanceByStudent(student);

            System.out.println("Found " + attendanceRecords.size() + " attendance records for " + student.getFullName());

            // Calculate attendance statistics
            long totalClasses = attendanceRecords.size();
            long presentCount = attendanceRecords.stream().filter(a -> "PRESENT".equals(a.getStatus())).count();
            long absentCount = attendanceRecords.stream().filter(a -> "ABSENT".equals(a.getStatus())).count();
            long lateCount = attendanceRecords.stream().filter(a -> "LATE".equals(a.getStatus())).count();

            double attendancePercentage = totalClasses > 0 ? (double) presentCount / totalClasses * 100 : 0;

            model.addAttribute("student", student);
            model.addAttribute("attendanceRecords", attendanceRecords);
            model.addAttribute("totalClasses", totalClasses);
            model.addAttribute("presentCount", presentCount);
            model.addAttribute("absentCount", absentCount);
            model.addAttribute("lateCount", lateCount);
            model.addAttribute("attendancePercentage", String.format("%.1f", attendancePercentage));

            return "student/attendance";

        } catch (Exception e) {
            System.out.println("ERROR loading student attendance: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Error loading attendance records: " + e.getMessage());
            return "student/attendance";
        }
    }
}