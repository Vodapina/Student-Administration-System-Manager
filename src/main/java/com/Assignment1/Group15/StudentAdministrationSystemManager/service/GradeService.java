package com.Assignment1.Group15.StudentAdministrationSystemManager.service;

import com.Assignment1.Group15.StudentAdministrationSystemManager.entity.Grade;
import com.Assignment1.Group15.StudentAdministrationSystemManager.entity.Student;
import com.Assignment1.Group15.StudentAdministrationSystemManager.entity.Subject;
import com.Assignment1.Group15.StudentAdministrationSystemManager.repository.GradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class GradeService {

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private SubjectService subjectService;

    // Create or update a grade
    public Grade saveGrade(Grade grade) {
        return gradeRepository.save(grade);
    }

    // Enter grade for a student in Biology
    public Grade enterBiologyGrade(Student student, String gradeValue, String term) {
        System.out.println("=== DEBUG: Entering Biology Grade ===");
        System.out.println("Student: " + student.getFullName() + " (ID: " + student.getId() + ")");
        System.out.println("Grade: " + gradeValue + ", Term: " + term);

        // Get Biology subject
        Subject biology = subjectService.getSubjectByCode("BIO6")
                .orElseThrow(() -> new RuntimeException("Biology subject not found"));

        System.out.println("Biology subject: " + biology.getSubjectName() + " (ID: " + biology.getId() + ")");

        // Check if grade already exists for this term
        Optional<Grade> existingGrade = gradeRepository.findByStudentAndSubjectAndTerm(student, biology, term);

        if (existingGrade.isPresent()) {
            // Update existing grade
            System.out.println("Updating existing grade...");
            Grade grade = existingGrade.get();
            grade.setGrade(gradeValue);
            Grade savedGrade = gradeRepository.save(grade);
            System.out.println("✅ Grade UPDATED successfully! Grade ID: " + savedGrade.getId());
            return savedGrade;
        } else {
            // Create new grade
            System.out.println("Creating new grade...");
            Grade grade = new Grade(student, biology, gradeValue, term);
            Grade savedGrade = gradeRepository.save(grade);
            System.out.println("✅ Grade CREATED successfully! Grade ID: " + savedGrade.getId());
            return savedGrade;
        }
    }

    // Get all grades for a student
    public List<Grade> getGradesByStudent(Student student) {
        return gradeRepository.findByStudent(student);
    }

    // Get Biology grades for a student
    public List<Grade> getBiologyGradesByStudent(Student student) {
        Subject biology = subjectService.getSubjectByCode("BIO6")
                .orElseThrow(() -> new RuntimeException("Biology subject not found"));
        return gradeRepository.findByStudentAndSubject(student, biology);
    }

    // Get all Biology grades (for teacher view)
    public List<Grade> getAllBiologyGrades() {
        Subject biology = subjectService.getSubjectByCode("BIO6")
                .orElseThrow(() -> new RuntimeException("Biology subject not found"));
        return gradeRepository.findBySubject(biology);
    }

    // Get grade by ID
    public Optional<Grade> getGradeById(Long id) {
        return gradeRepository.findById(id);
    }

    // Delete grade
    public void deleteGrade(Long id) {
        gradeRepository.deleteById(id);
    }
}