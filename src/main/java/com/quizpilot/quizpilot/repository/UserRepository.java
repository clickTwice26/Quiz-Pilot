package com.quizpilot.quizpilot.repository;

import com.quizpilot.quizpilot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Spring Data JPA automatically provides CRUD operations (Create, Read, Update, Delete)
    
    // Custom query method to find user by username
    Optional<User> findByUsername(String username);
    
    // You can add more custom query methods here if needed
}