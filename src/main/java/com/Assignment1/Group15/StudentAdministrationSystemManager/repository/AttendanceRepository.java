package com.Assignment1.Group15.StudentAdministrationSystemManager.repository;

import com.Assignment1.Group15.StudentAdministrationSystemManager.entity.Attendance;
import com.Assignment1.Group15.StudentAdministrationSystemManager.entity.Student;
import com.Assignment1.Group15.StudentAdministrationSystemManager.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByStudent(Student student);
    List<Attendance> findByStudentAndSubject(Student student, Subject subject);
    List<Attendance> findBySubject(Subject subject);
    List<Attendance> findByDateAndSubject(LocalDate date, Subject subject);
    boolean existsByStudentAndSubjectAndDate(Student student, Subject subject, LocalDate date);
}