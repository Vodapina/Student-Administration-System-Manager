package com.Assignment1.Group15.StudentAdministrationSystemManager.service;

import com.Assignment1.Group15.StudentAdministrationSystemManager.entity.Student;
import com.Assignment1.Group15.StudentAdministrationSystemManager.entity.User;
import com.Assignment1.Group15.StudentAdministrationSystemManager.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserService userService;

    // Create a new student
    public Student createStudent(User user, String studentId, String firstName, String lastName) {
        Student student = new Student(user, studentId, firstName, lastName);
        return studentRepository.save(student);
    }

    // Get all students
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // Get student by ID
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    // Get student by student ID
    public Optional<Student> getStudentByStudentId(String studentId) {
        return studentRepository.findByStudentId(studentId);
    }

    // Get students by form level
    public List<Student> getStudentsByFormLevel(String formLevel) {
        return studentRepository.findByFormLevel(formLevel);
    }

    // In StudentService.java - add this method:
    public Optional<Student> getStudentByUserUsername(String username) {
        return studentRepository.findByUser_Username(username);
    }

    // Update student
    public Student updateStudent(Student student) {
        return studentRepository.save(student);
    }

    // Delete student
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    // Check if student ID exists
    public boolean studentIdExists(String studentId) {
        return studentRepository.existsByStudentId(studentId);
    }
}