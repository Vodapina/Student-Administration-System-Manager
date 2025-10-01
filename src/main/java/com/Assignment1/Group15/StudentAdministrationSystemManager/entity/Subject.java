package com.Assignment1.Group15.StudentAdministrationSystemManager.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "subjects")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String subjectCode; // e.g., "BIO6", "CHE6", "PHY6"

    @Column(nullable = false)
    private String subjectName;

    private String description;
    private String formLevel = "6";
    private String stream = "SCIENCE";

    // Constructors
    public Subject() {}

    public Subject(String subjectCode, String subjectName, String description) {
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.description = description;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSubjectCode() { return subjectCode; }
    public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }

    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getFormLevel() { return formLevel; }
    public void setFormLevel(String formLevel) { this.formLevel = formLevel; }

    public String getStream() { return stream; }
    public void setStream(String stream) { this.stream = stream; }

    @Override
    public String toString() {
        return "Subject{" +
                "id=" + id +
                ", subjectCode='" + subjectCode + '\'' +
                ", subjectName='" + subjectName + '\'' +
                ", formLevel='" + formLevel + '\'' +
                ", stream='" + stream + '\'' +
                '}';
    }
}