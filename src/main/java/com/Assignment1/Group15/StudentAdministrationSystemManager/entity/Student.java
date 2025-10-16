package com.Assignment1.Group15.StudentAdministrationSystemManager.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @Column(unique = true, nullable = false)
    private String studentId; // This will be the username (e.g., "S11200631")

    private String email; // Auto-generated email (e.g., "s11200631@student.com")

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private LocalDate dateOfBirth;
    private String address;
    private String parentName;
    private String parentPhone;

    private String formLevel = "6";
    private String scienceStream = "SCIENCE";

    // Constructors
    public Student() {}

    public Student(User user, String studentId, String firstName, String lastName) {
        this.user = user;
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = generateEmailFromStudentId(studentId);
    }

    public Student(String studentId, String firstName, String lastName) {
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = generateEmailFromStudentId(studentId);
    }

    // Auto-generate email from student ID
    private String generateEmailFromStudentId(String studentId) {
        if (studentId == null || studentId.trim().isEmpty()) {
            return null;
        }
        return studentId.toLowerCase() + "@student.com";
    }

    // Auto-generate username from student ID (same as studentId)
    public String getUsername() {
        return this.studentId;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) {
        this.studentId = studentId;
        // Auto-update email when student ID changes
        if (studentId != null && !studentId.trim().isEmpty()) {
            this.email = generateEmailFromStudentId(studentId);
        }
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getParentName() { return parentName; }
    public void setParentName(String parentName) { this.parentName = parentName; }

    public String getParentPhone() { return parentPhone; }
    public void setParentPhone(String parentPhone) { this.parentPhone = parentPhone; }

    public String getFormLevel() { return formLevel; }
    public void setFormLevel(String formLevel) { this.formLevel = formLevel; }

    public String getScienceStream() { return scienceStream; }
    public void setScienceStream(String scienceStream) { this.scienceStream = scienceStream; }

    // Helper methods
    public String getFullName() {
        if (firstName == null && lastName == null) {
            return studentId; // Fallback to student ID if names are null
        }
        return (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "");
    }

    public String getGeneratedCredentials() {
        return String.format("Username: %s | Email: %s", getUsername(), getEmail());
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", studentId='" + studentId + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", formLevel='" + formLevel + '\'' +
                ", scienceStream='" + scienceStream + '\'' +
                '}';
    }
}