package com.Assignment1.Group15.StudentAdministrationSystemManager.service;

import com.Assignment1.Group15.StudentAdministrationSystemManager.entity.Attendance;
import com.Assignment1.Group15.StudentAdministrationSystemManager.entity.Student;
import com.Assignment1.Group15.StudentAdministrationSystemManager.entity.Subject;
import com.Assignment1.Group15.StudentAdministrationSystemManager.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private SubjectService subjectService;

    // Mark attendance for a student in Biology
    public Attendance markBiologyAttendance(Student student, LocalDate date, String status) {
        // Get Biology subject - AUTO-CREATE if doesn't exist
        Subject biology = subjectService.getSubjectByCode("BIO6")
                .orElseGet(() -> {
                    System.out.println("Biology subject not found - creating it automatically");
                    return subjectService.createSubject("BIO6", "Biology", "Form 6 Biology");
                });

        // Check if attendance already exists for this date
        Optional<Attendance> existingAttendance = attendanceRepository
                .findByStudentAndSubjectAndDate(student, biology, date);

        if (existingAttendance.isPresent()) {
            // Update existing attendance
            Attendance attendance = existingAttendance.get();
            attendance.setStatus(status);
            System.out.println("Updated attendance for " + student.getFullName() + ": " + status);
            return attendanceRepository.save(attendance);
        } else {
            // Create new attendance
            Attendance attendance = new Attendance(student, biology, date, status);
            System.out.println("Created new attendance for " + student.getFullName() + ": " + status);
            return attendanceRepository.save(attendance);
        }
    }

    // Get all Biology attendance for a student
    public List<Attendance> getBiologyAttendanceByStudent(Student student) {
        Subject biology = subjectService.getSubjectByCode("BIO6")
                .orElseGet(() -> subjectService.createSubject("BIO6", "Biology", "Form 6 Biology"));
        return attendanceRepository.findByStudentAndSubject(student, biology);
    }

    // Get all Biology attendance for a specific date
    public List<Attendance> getBiologyAttendanceByDate(LocalDate date) {
        Subject biology = subjectService.getSubjectByCode("BIO6")
                .orElseGet(() -> subjectService.createSubject("BIO6", "Biology", "Form 6 Biology"));
        return attendanceRepository.findByDateAndSubject(date, biology);
    }

    // Get attendance by ID
    public Optional<Attendance> getAttendanceById(Long id) {
        return attendanceRepository.findById(id);
    }

    // Save attendance
    public Attendance saveAttendance(Attendance attendance) {
        return attendanceRepository.save(attendance);
    }

    // Delete attendance
    public void deleteAttendance(Long id) {
        attendanceRepository.deleteById(id);
    }
}