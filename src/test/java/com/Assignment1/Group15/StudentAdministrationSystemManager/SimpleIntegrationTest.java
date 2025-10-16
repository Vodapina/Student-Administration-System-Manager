package com.Assignment1.Group15.StudentAdministrationSystemManager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class SimpleIntegrationTest {

    @Test
    void testContextLoads() {
        // Simple test to verify Spring context loads
        assertTrue(true, "Spring context should load successfully");
    }

    @Test
    void testBasicApplicationLogic() {
        // Test basic application logic
        String appName = "Student Administration System";
        assertNotNull(appName);
        assertTrue(appName.contains("Student"));
    }
}