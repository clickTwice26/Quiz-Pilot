package com.quizpilot.quizpilot.service;

import com.quizpilot.quizpilot.model.User;
import com.quizpilot.quizpilot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Verify if the session token is valid and return the associated user
     * @param sessionToken The session token to verify
     * @return Optional<User> containing the user if token is valid, empty otherwise
     */
    public Optional<User> verifyToken(String sessionToken) {
        if (sessionToken == null || sessionToken.trim().isEmpty()) {
            return Optional.empty();
        }
        
        return userRepository.findBySessionToken(sessionToken);
    }

    /**
     * Extract token from Authorization header
     * @param authHeader The Authorization header value
     * @return The token or null if invalid format
     */
    public String extractToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // Remove "Bearer " prefix
        }
        return null;
    }
}
