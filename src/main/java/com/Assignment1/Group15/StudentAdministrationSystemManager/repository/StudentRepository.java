package com.Assignment1.Group15.StudentAdministrationSystemManager.repository;

import com.Assignment1.Group15.StudentAdministrationSystemManager.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByStudentId(String studentId);
    Optional<Student> findByUser_Username(String username);
    List<Student> findByFormLevel(String formLevel);
    List<Student> findByScienceStream(String scienceStream);
    boolean existsByStudentId(String studentId);
}