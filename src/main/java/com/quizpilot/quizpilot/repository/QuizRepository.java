package com.quizpilot.quizpilot.repository;

import com.quizpilot.quizpilot.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    // Spring Data JPA automatically provides CRUD operations (Create, Read, Update, Delete)
    // You can add custom query methods here if needed, e.g., findByTitle(String title)
}