package com.Assignment1.Group15.StudentAdministrationSystemManager.repository;

import com.Assignment1.Group15.StudentAdministrationSystemManager.entity.Grade;
import com.Assignment1.Group15.StudentAdministrationSystemManager.entity.Student;
import com.Assignment1.Group15.StudentAdministrationSystemManager.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
    List<Grade> findByStudent(Student student);
    List<Grade> findByStudentAndSubject(Student student, Subject subject);
    List<Grade> findBySubject(Subject subject);
    Optional<Grade> findByStudentAndSubjectAndTerm(Student student, Subject subject, String term);
}