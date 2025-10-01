package com.Assignment1.Group15.StudentAdministrationSystemManager.service;

import com.Assignment1.Group15.StudentAdministrationSystemManager.entity.Subject;
import com.Assignment1.Group15.StudentAdministrationSystemManager.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    // Create a new subject
    public Subject createSubject(String subjectCode, String subjectName, String description) {
        Subject subject = new Subject(subjectCode, subjectName, description);
        return subjectRepository.save(subject);
    }

    // Get all subjects
    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    // Get subject by ID
    public Optional<Subject> getSubjectById(Long id) {
        return subjectRepository.findById(id);
    }

    // Get subject by subject code
    public Optional<Subject> getSubjectByCode(String subjectCode) {
        return subjectRepository.findBySubjectCode(subjectCode);
    }

    // Get subjects by form level
    public List<Subject> getSubjectsByFormLevel(String formLevel) {
        return subjectRepository.findByFormLevel(formLevel);
    }

    // Update subject
    public Subject updateSubject(Subject subject) {
        return subjectRepository.save(subject);
    }

    // Delete subject
    public void deleteSubject(Long id) {
        subjectRepository.deleteById(id);
    }

    // Check if subject code exists
    public boolean subjectCodeExists(String subjectCode) {
        return subjectRepository.existsBySubjectCode(subjectCode);
    }
}