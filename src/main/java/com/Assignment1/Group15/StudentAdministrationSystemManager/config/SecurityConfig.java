package com.Assignment1.Group15.StudentAdministrationSystemManager.config;

import com.Assignment1.Group15.StudentAdministrationSystemManager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserRepository userRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username)
                .map(user -> org.springframework.security.core.userdetails.User.builder()
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .roles(user.getRole())
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Public routes
                        .requestMatchers("/", "/register", "/css/**", "/js/**", "/images/**").permitAll()

                        // Admin routes - only ADMIN can access
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // Teacher routes - only TEACHER can access
                        .requestMatchers("/teacher/**").hasRole("TEACHER")

                        // Student routes - only STUDENT can access
                        .requestMatchers("/student/**").hasRole("STUDENT")

                        // Student management - ADMIN and TEACHER can access
                        .requestMatchers("/students/**").hasAnyRole("ADMIN", "TEACHER")

                        // Dashboard access based on role
                        .requestMatchers("/dashboard").authenticated()

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/dashboard", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/?logout")
                        .permitAll()
                );

        return http.build();
    }
}