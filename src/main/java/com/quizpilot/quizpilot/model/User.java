package com.quizpilot.quizpilot.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@Table(name = "app_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(User.UserEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String username;
    
    private String fullname;
    private String userRole;
    private String sessionToken;
    
    @Column(nullable = false)
    private String password;

    @Transient
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void setPassword(String password) {
        this.password = passwordEncoder.encode(password);
    }

    public boolean matchPassword(String rawPassword) {
        return passwordEncoder.matches(rawPassword, this.password);
    }

    public static class UserEntityListener {
        @PrePersist
        @PreUpdate
        public void hashPassword(User user) {
            // Only hash the password if it's not already hashed
            if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
                user.setPassword(user.getPassword());
            }
        }
    }

    // You can add more fields as needed for your quiz application
}