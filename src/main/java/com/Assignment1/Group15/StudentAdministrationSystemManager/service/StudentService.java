package com.Assignment1.Group15.StudentAdministrationSystemManager.service;

import com.Assignment1.Group15.StudentAdministrationSystemManager.entity.Student;
import com.Assignment1.Group15.StudentAdministrationSystemManager.entity.User;
import com.Assignment1.Group15.StudentAdministrationSystemManager.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;
@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Create a new student with auto-generated credentials
    public Student registerStudent(Student student) {
        return registerStudent(student, "defaultPassword123");
    }

    // Create a new student with custom temporary password
    public Student registerStudent(Student student, String temporaryPassword) {
        // Validate student ID
        if (student.getStudentId() == null || student.getStudentId().trim().isEmpty()) {
            throw new IllegalArgumentException("Student ID cannot be null or empty");
        }

        // Check if student ID already exists
        if (studentIdExists(student.getStudentId())) {
            throw new RuntimeException("Student ID already exists: " + student.getStudentId());
        }

        // Check if username already exists
        if (userService.findByUsername(student.getUsername()) != null) {
            throw new RuntimeException("Username already exists: " + student.getUsername());
        }

        // Generate user account with auto-generated credentials
        User user = userService.registerUser(
                student.getUsername(), // This uses studentId as username
                temporaryPassword,
                student.getEmail(),    // Auto-generated email from entity
                "STUDENT"
        );

        // Link user to student
        student.setUser(user);

        return studentRepository.save(student);
    }


    // Legacy method - kept for backward compatibility
    public Student createStudent(User user, String studentId, String firstName, String lastName) {
        Student student = new Student(user, studentId, firstName, lastName);
        return studentRepository.save(student);
    }

    // Create student with minimal details (auto-generates everything else)
    public Student createStudentWithId(String studentId, String firstName, String lastName) {
        Student student = new Student(studentId, firstName, lastName);
        return registerStudent(student);
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

    // Get student by user username
    public Optional<Student> getStudentByUserUsername(String username) {
        return studentRepository.findByUser_Username(username);
    }

    // Update student
    public Student updateStudent(Student student) {
        // If student ID changed, update the email and username
        Student existingStudent = studentRepository.findById(student.getId())
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + student.getId()));

        // If student ID changed, we need to update the associated user
        if (!existingStudent.getStudentId().equals(student.getStudentId())) {
            // Update the user's username and email
            User user = existingStudent.getUser();
            user.setUsername(student.getUsername());
            user.setEmail(student.getEmail());
            userService.updateUser(user);
        }

        return studentRepository.save(student);
    }


    // Delete student (also deletes the associated user account)
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));

        // Delete associated user account
        if (student.getUser() != null) {
            userService.deleteUser(student.getUser().getId());
        }

        studentRepository.deleteById(id);
    }

    // Check if student ID exists
    public boolean studentIdExists(String studentId) {
        return studentRepository.existsByStudentId(studentId);
    }

    // Get generated credentials for a student
    public String getStudentCredentials(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

        return student.getGeneratedCredentials();
    }

    // Reset student password
    public void resetStudentPassword(Long studentId, String newPassword) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

        if (student.getUser() != null) {
            userService.resetPassword(student.getUser().getId(), newPassword);
        }
    }
}