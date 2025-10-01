package com.Assignment1.Group15.StudentAdministrationSystemManager.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "grades")
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @Column(nullable = false)
    private String grade; // A, B, C, D, F

    private String term; // Term 1, Term 2, Term 3, Final
    private String comments;
    private LocalDateTime createdAt;

    // Constructors
    public Grade() {
        this.createdAt = LocalDateTime.now();
    }

    public Grade(Student student, Subject subject, String grade, String term) {
        this();
        this.student = student;
        this.subject = subject;
        this.grade = grade;
        this.term = term;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public Subject getSubject() { return subject; }
    public void setSubject(Subject subject) { this.subject = subject; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public String getTerm() { return term; }
    public void setTerm(String term) { this.term = term; }

    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}