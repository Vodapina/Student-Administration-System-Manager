package com.Assignment1.Group15.StudentAdministrationSystemManager.repository;

import com.Assignment1.Group15.StudentAdministrationSystemManager.entity.Attendance;
import com.Assignment1.Group15.StudentAdministrationSystemManager.entity.Student;
import com.Assignment1.Group15.StudentAdministrationSystemManager.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    // Find attendance by student, subject, and date
    Optional<Attendance> findByStudentAndSubjectAndDate(Student student, Subject subject, LocalDate date);

    // Find all attendance records for a specific student
    List<Attendance> findByStudent(Student student);

    // Find all attendance records for a specific student and subject
    List<Attendance> findByStudentAndSubject(Student student, Subject subject);

    // Find all attendance records for a specific subject
    List<Attendance> findBySubject(Subject subject);

    // Find all attendance records for a specific date and subject
    List<Attendance> findByDateAndSubject(LocalDate date, Subject subject);

    // Find all attendance records for a specific date
    List<Attendance> findByDate(LocalDate date);

    // Check if attendance exists for student, subject, and date
    boolean existsByStudentAndSubjectAndDate(Student student, Subject subject, LocalDate date);

    // Find attendance by student and date range (optional, for future use)
    List<Attendance> findByStudentAndDateBetween(Student student, LocalDate startDate, LocalDate endDate);
}