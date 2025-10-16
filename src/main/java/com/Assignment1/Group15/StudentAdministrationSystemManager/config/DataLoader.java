package com.Assignment1.Group15.StudentAdministrationSystemManager.config;

import com.Assignment1.Group15.StudentAdministrationSystemManager.entity.Subject;
import com.Assignment1.Group15.StudentAdministrationSystemManager.entity.User;
import com.Assignment1.Group15.StudentAdministrationSystemManager.service.SubjectService;
import com.Assignment1.Group15.StudentAdministrationSystemManager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private SubjectService subjectService;

    @Override
    public void run(String... args) throws Exception {
        // Create Form 6 Science Subjects
        if (subjectService.getAllSubjects().isEmpty()) {
            subjectService.createSubject("BIO6", "Biology", "Form 6 Biology");
            subjectService.createSubject("CHE6", "Chemistry", "Form 6 Chemistry");
            subjectService.createSubject("PHY6", "Physics", "Form 6 Physics");
            subjectService.createSubject("MAT6", "Mathematics", "Form 6 Mathematics");
            subjectService.createSubject("COM6", "Computer Studies", "Form 6 Computer Studies");
            subjectService.createSubject("ENG6", "English", "Form 6 English");

            System.out.println("Form 6 Science subjects created!");
        }

        // Create sample admin user if not exists
        if (userService.findByUsername("admin") == null) {
            userService.registerUser("admin", "admin123", "admin@school.com", "ADMIN");
            System.out.println(" Admin user created!");
        }
    }
}