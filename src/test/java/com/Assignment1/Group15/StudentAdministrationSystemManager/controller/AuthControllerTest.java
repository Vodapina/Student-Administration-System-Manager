package com.Assignment1.Group15.StudentAdministrationSystemManager.controller;

import com.Assignment1.Group15.StudentAdministrationSystemManager.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    void testShowLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void testShowRegistrationForm() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    @Test
    void testAdminPortal_WhenNoAdminExists() throws Exception {
        // Given - no admin exists
        when(userService.doesAdminExist()).thenReturn(false);

        // When & Then
        mockMvc.perform(get("/admin/portal"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/portal"))
                .andExpect(model().attribute("allowRegistration", true));
    }

    @Test
    void testAdminPortal_WhenAdminExists() throws Exception {
        // Given - admin exists
        when(userService.doesAdminExist()).thenReturn(true);

        // When & Then
        mockMvc.perform(get("/admin/portal"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/portal"))
                .andExpect(model().attribute("allowRegistration", false));
    }
}