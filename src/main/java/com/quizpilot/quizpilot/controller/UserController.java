package com.quizpilot.quizpilot.controller;

import com.quizpilot.quizpilot.dto.UserDto;
import com.quizpilot.quizpilot.dto.UserRegistrationDto;
import com.quizpilot.quizpilot.dto.LoginDto;
import com.quizpilot.quizpilot.model.User;
import com.quizpilot.quizpilot.repository.UserRepository;
import com.quizpilot.quizpilot.responses.*;
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



@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "User registration, authentication, and profile management")
public class UserController {

    @Autowired
    private UserRepository userRepository;


    private String getAccessToken(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder token = new StringBuilder();
    
        for (int i = 0; i < length; i++) {
            int randomIndex = (int) (Math.random() * characters.length());
            token.append(characters.charAt(randomIndex));
        }
    
        return token.toString();
    }
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
        user.setEmailAddress(registrationDto.getEmailAddress());
        
        User savedUser = userRepository.save(user);
        return ResponseEntity.status(201).body(convertToDto(savedUser));
    }

    @Operation(summary = "Login a user", description = "Retrieve user sessionToken by providing correct credentials")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login Successful"),
        @ApiResponse(responseCode = "401", description = "Login Failure, Invalid credentials"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(
            @Parameter(description = "User login data", required = true)
            @RequestBody LoginDto loginCred) {
        
        try {
            Optional<User> userOptional = userRepository.findByEmailAddress(loginCred.getEmailAddress());
            
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(401).body(new LoginResponse("401", "User not found", ""));
            }
            
            User loginUser = userOptional.get();
            
            if (loginUser.matchPassword(loginCred.getPassword())) {
                String sessionToken = "session_" + this.getAccessToken(192);
                
                loginUser.setSessionToken(sessionToken);
                userRepository.save(loginUser);
                
                LoginResponse response = new LoginResponse("200", loginUser.getEmailAddress(), sessionToken);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(401).body(new LoginResponse("401", "Invalid credentials", ""));
            }
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new LoginResponse("500", "Internal server error", ""));
        }
    }
    @Operation(summary = "Get all available user data", description = "For development purpose you can get all the information of a user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login Successful"),
        @ApiResponse(responseCode = "401", description = "Login Failure, Invalid credentials"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        try{
            List<User> users = userRepository.findAll();
            return ResponseEntity.ok(users);
        }catch (Exception e){
            return ResponseEntity.status(500).build();

        }

    }
    
    private UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setFullname(user.getFullname());
        dto.setEmailAddress(user.getEmailAddress());
        dto.setUserRole(user.getUserRole());
        return dto;
    }

}