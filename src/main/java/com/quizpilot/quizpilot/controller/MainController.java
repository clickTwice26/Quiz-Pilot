package com.quizpilot.quizpilot.controller;

import com.quizpilot.quizpilot.dto.AuthenticatedResponse;
import com.quizpilot.quizpilot.dto.UserDto;
import com.quizpilot.quizpilot.model.User;
import com.quizpilot.quizpilot.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@Tag(name = "Main Controller", description = "Core API endpoints for health checks, application info, and service status")
public class MainController {

    @Autowired
    private AuthService authService;
    
    // Health check endpoint
    @Operation(summary = "Health Check (Secured)", description = "Check the health status of the QuizPilot service - requires authentication")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Service is healthy and user authenticated"),
        @ApiResponse(responseCode = "401", description = "Invalid or missing token")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/health")
    public ResponseEntity<AuthenticatedResponse<Map<String, String>>> healthCheck(
            @Parameter(description = "Authorization header with Bearer token", required = true)
            @RequestHeader("Authorization") String authHeader) {
        
        String token = authService.extractToken(authHeader);
        Optional<User> userOptional = authService.verifyToken(token);
        
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(401).body(
                new AuthenticatedResponse<>("401", "Invalid or missing token", null, null)
            );
        }
        
        Map<String, String> healthData = new HashMap<>();
        healthData.put("status", "UP");
        healthData.put("service", "QuizPilot");
        healthData.put("timestamp", String.valueOf(System.currentTimeMillis()));
        
        UserDto userDto = convertToDto(userOptional.get());
        AuthenticatedResponse<Map<String, String>> response = new AuthenticatedResponse<>(
            "200", "Service is healthy", userDto, healthData
        );
        
        return ResponseEntity.ok(response);
    }
    
    // Application info endpoint
    @Operation(summary = "Application Information (Secured)", description = "Get detailed information about the QuizPilot application - requires authentication")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Application information retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Invalid or missing token")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/info")
    public ResponseEntity<AuthenticatedResponse<Map<String, Object>>> appInfo(
            @Parameter(description = "Authorization header with Bearer token", required = true)
            @RequestHeader("Authorization") String authHeader) {
        
        String token = authService.extractToken(authHeader);
        Optional<User> userOptional = authService.verifyToken(token);
        
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(401).body(
                new AuthenticatedResponse<>("401", "Invalid or missing token", null, null)
            );
        }
        
        Map<String, Object> appData = new HashMap<>();
        appData.put("application", "QuizPilot");
        appData.put("version", "1.0.0");
        appData.put("description", "Interactive Quiz Application API");
        appData.put("apiVersion", "v1");
        
        UserDto userDto = convertToDto(userOptional.get());
        AuthenticatedResponse<Map<String, Object>> response = new AuthenticatedResponse<>(
            "200", "Application information retrieved successfully", userDto, appData
        );
        
        return ResponseEntity.ok(response);
    }
    
    // Welcome endpoint (root API)
    @Operation(summary = "Welcome Message (Secured)", description = "Get welcome message and available endpoints overview - requires authentication")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Welcome message with endpoints list"),
        @ApiResponse(responseCode = "401", description = "Invalid or missing token")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/")
    public ResponseEntity<AuthenticatedResponse<Map<String, Object>>> welcome(
            @Parameter(description = "Authorization header with Bearer token", required = true)
            @RequestHeader("Authorization") String authHeader) {
        
        String token = authService.extractToken(authHeader);
        Optional<User> userOptional = authService.verifyToken(token);
        
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(401).body(
                new AuthenticatedResponse<>("401", "Invalid or missing token", null, null)
            );
        }
        
        Map<String, Object> welcomeData = new HashMap<>();
        welcomeData.put("message", "Welcome to QuizPilot API");
        welcomeData.put("status", "active");
        welcomeData.put("endpoints", getAvailableEndpoints());
        
        UserDto userDto = convertToDto(userOptional.get());
        AuthenticatedResponse<Map<String, Object>> response = new AuthenticatedResponse<>(
            "200", "Welcome message retrieved successfully", userDto, welcomeData
        );
        
        return ResponseEntity.ok(response);
    }
    
    // Get available API endpoints
    @Operation(summary = "List API Endpoints (Secured)", description = "Get a comprehensive list of all available API endpoints - requires authentication")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Endpoints list retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Invalid or missing token")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/endpoints")
    public ResponseEntity<AuthenticatedResponse<Map<String, Object>>> getEndpoints(
            @Parameter(description = "Authorization header with Bearer token", required = true)
            @RequestHeader("Authorization") String authHeader) {
        
        String token = authService.extractToken(authHeader);
        Optional<User> userOptional = authService.verifyToken(token);
        
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(401).body(
                new AuthenticatedResponse<>("401", "Invalid or missing token", null, null)
            );
        }
        
        Map<String, Object> endpointData = new HashMap<>();
        endpointData.put("endpoints", getAvailableEndpoints());
        endpointData.put("baseUrl", "/api");
        
        UserDto userDto = convertToDto(userOptional.get());
        AuthenticatedResponse<Map<String, Object>> response = new AuthenticatedResponse<>(
            "200", "Endpoints list retrieved successfully", userDto, endpointData
        );
        
        return ResponseEntity.ok(response);
    }
    
    // API status endpoint
    @Operation(summary = "Service Status (Secured)", description = "Get current operational status of the QuizPilot service - requires authentication")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Service status retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Invalid or missing token")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/status")
    public ResponseEntity<AuthenticatedResponse<Map<String, Object>>> getStatus(
            @Parameter(description = "Authorization header with Bearer token", required = true)
            @RequestHeader("Authorization") String authHeader) {
        
        String token = authService.extractToken(authHeader);
        Optional<User> userOptional = authService.verifyToken(token);
        
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(401).body(
                new AuthenticatedResponse<>("401", "Invalid or missing token", null, null)
            );
        }
        
        Map<String, Object> statusData = new HashMap<>();
        statusData.put("status", "operational");
        statusData.put("uptime", "Running");
        statusData.put("database", "connected");
        statusData.put("lastUpdated", String.valueOf(System.currentTimeMillis()));
        
        UserDto userDto = convertToDto(userOptional.get());
        AuthenticatedResponse<Map<String, Object>> response = new AuthenticatedResponse<>(
            "200", "Service status retrieved successfully", userDto, statusData
        );
        
        return ResponseEntity.ok(response);
    }
    
    // Helper method to list available endpoints
    private List<Map<String, String>> getAvailableEndpoints() {
        List<Map<String, String>> endpoints = new ArrayList<>();
        
        Map<String, String> health = new HashMap<>();
        health.put("path", "/api/health");
        health.put("method", "GET");
        health.put("description", "Health check endpoint");
        endpoints.add(health);
        
        Map<String, String> info = new HashMap<>();
        info.put("path", "/api/info");
        info.put("method", "GET");
        info.put("description", "Application information");
        endpoints.add(info);
        
        Map<String, String> status = new HashMap<>();
        status.put("path", "/api/status");
        status.put("method", "GET");
        status.put("description", "Service status information");
        endpoints.add(status);
        
        Map<String, String> endpointsList = new HashMap<>();
        endpointsList.put("path", "/api/endpoints");
        endpointsList.put("method", "GET");
        endpointsList.put("description", "List all available endpoints");
        endpoints.add(endpointsList);
        
        return endpoints;
    }
    
    // Helper method to convert User entity to UserDto
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