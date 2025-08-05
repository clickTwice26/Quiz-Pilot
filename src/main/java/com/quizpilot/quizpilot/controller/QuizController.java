package com.quizpilot.quizpilot.controller;

import com.quizpilot.quizpilot.model.Quiz;
import com.quizpilot.quizpilot.repository.QuizRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/quizzes")
@Tag(name = "Quiz Management", description = "CRUD operations for quiz management")
public class QuizController {

    @Autowired
    private QuizRepository quizRepository;

    @Operation(summary = "Get all quizzes", description = "Retrieve a list of all available quizzes")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Quizzes retrieved successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<Quiz>> getAllQuizzes() {
        List<Quiz> quizzes = quizRepository.findAll();
        return ResponseEntity.ok(quizzes);
    }

    @Operation(summary = "Get quiz by ID", description = "Retrieve a specific quiz by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Quiz found and retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Quiz not found"),
        @ApiResponse(responseCode = "400", description = "Invalid quiz ID")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Quiz> getQuizById(
            @Parameter(description = "ID of the quiz to retrieve", required = true)
            @PathVariable Long id) {
        Optional<Quiz> quiz = quizRepository.findById(id);
        return quiz.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new quiz", description = "Create a new quiz with title and description")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Quiz created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid quiz data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<Quiz> createQuiz(
            @Parameter(description = "Quiz object to be created", required = true)
            @RequestBody Quiz quiz) {
        Quiz savedQuiz = quizRepository.save(quiz);
        return ResponseEntity.status(201).body(savedQuiz);
    }

    @Operation(summary = "Update an existing quiz", description = "Update a quiz's information by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Quiz updated successfully"),
        @ApiResponse(responseCode = "404", description = "Quiz not found"),
        @ApiResponse(responseCode = "400", description = "Invalid quiz data")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Quiz> updateQuiz(
            @Parameter(description = "ID of the quiz to update", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated quiz object", required = true)
            @RequestBody Quiz quizDetails) {
        return quizRepository.findById(id)
                .map(quiz -> {
                    quiz.setTitle(quizDetails.getTitle());
                    quiz.setDescription(quizDetails.getDescription());
                    return ResponseEntity.ok(quizRepository.save(quiz));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete a quiz", description = "Delete a quiz by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Quiz deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Quiz not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuiz(
            @Parameter(description = "ID of the quiz to delete", required = true)
            @PathVariable Long id) {
        return quizRepository.findById(id)
                .map(quiz -> {
                    quizRepository.delete(quiz);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
