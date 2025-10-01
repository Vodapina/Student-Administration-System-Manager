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
        // Get Biology subject
        Subject biology = subjectService.getSubjectByCode("BIO6")
                .orElseThrow(() -> new RuntimeException("Biology subject not found"));

        // Check if grade already exists for this term
        Optional<Grade> existingGrade = gradeRepository.findByStudentAndSubjectAndTerm(student, biology, term);

        if (existingGrade.isPresent()) {
            // Update existing grade
            Grade grade = existingGrade.get();
            grade.setGrade(gradeValue);
            return gradeRepository.save(grade);
        } else {
            // Create new grade
            Grade grade = new Grade(student, biology, gradeValue, term);
            return gradeRepository.save(grade);
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