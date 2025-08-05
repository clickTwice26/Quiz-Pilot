package com.quizpilot.quizpilot.controller;

import com.quizpilot.quizpilot.dto.UserDto;
import com.quizpilot.quizpilot.dto.UserRegistrationDto;
import com.quizpilot.quizpilot.model.User;
import com.quizpilot.quizpilot.repository.UserRepository;
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
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "User registration, authentication, and profile management")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Operation(summary = "Register a new user", description = "Create a new user account with username, password, and profile information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User registered successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid user data or username already exists"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(
            @Parameter(description = "User registration data", required = true)
            @RequestBody UserRegistrationDto registrationDto) {
        
        // Check if username already exists
        if (userRepository.findByUsername(registrationDto.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().build();
        }
        
        User user = new User();
        user.setUsername(registrationDto.getUsername());
        user.setPassword(registrationDto.getPassword()); 
        user.setFullname(registrationDto.getFullname());
        user.setEmail_address(registrationDto.getEmail_address());
        
        User savedUser = userRepository.save(user);
        return ResponseEntity.status(201).body(convertToDto(savedUser));
    }

    @Operation(summary = "Login a user", description = "Retrieve user sessionToken by providing correct credentials")
    @ApiResponse(value={
        @ApiResponse(responseCode = "401", description = "Login Failure , Invalid credentials")
        @ApiResponse(responseCode = "200", description = "Login Successful"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/login")
    public String postMethodName(@RequestBody String entity) {
        //TODO: process POST request
        
        return entity;
    }
    



    @Operation(summary = "Get all users", description = "Retrieve a list of all registered users (excluding sensitive information)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDto> userDtos = users.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDtos);
    }

    @Operation(summary = "Get user by ID", description = "Retrieve a specific user by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User found and retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "400", description = "Invalid user ID")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(
            @Parameter(description = "ID of the user to retrieve", required = true)
            @PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(u -> ResponseEntity.ok(convertToDto(u)))
                  .orElse(ResponseEntity.notFound().build());
    }



    @Operation(summary = "Update user profile", description = "Update a user's profile information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User updated successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "400", description = "Invalid user data")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @Parameter(description = "ID of the user to update", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated user data", required = true)
            @RequestBody UserDto userDto) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setUsername(userDto.getUsername());
                    user.setFullname(userDto.getFullname());
                    user.setUserRole(userDto.getUserRole());
                    return ResponseEntity.ok(convertToDto(userRepository.save(user)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete a user", description = "Delete a user account by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID of the user to delete", required = true)
            @PathVariable Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    userRepository.delete(user);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get user by username", description = "Find a user by their username")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User found"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/username/{username}")
    public ResponseEntity<UserDto> getUserByUsername(
            @Parameter(description = "Username to search for", required = true)
            @PathVariable String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(u -> ResponseEntity.ok(convertToDto(u)))
                  .orElse(ResponseEntity.notFound().build());
    }

    // Helper method to convert User entity to UserDto
    private UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setFullname(user.getFullname());
        dto.setUserRole(user.getUserRole());
        return dto;
    }
}
