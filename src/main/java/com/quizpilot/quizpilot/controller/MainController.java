package com.quizpilot.quizpilot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/api")
@Tag(name = "Main Controller", description = "Core API endpoints for health checks, application info, and service status")
public class MainController {
    
    // Health check endpoint
    @Operation(summary = "Health Check", description = "Check the health status of the QuizPilot service")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Service is healthy")
    })
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "QuizPilot");
        response.put("timestamp", String.valueOf(System.currentTimeMillis()));
        return ResponseEntity.ok(response);
    }
    
    // Application info endpoint
    @Operation(summary = "Application Information", description = "Get detailed information about the QuizPilot application")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Application information retrieved successfully")
    })
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> appInfo() {
        Map<String, Object> response = new HashMap<>();
        response.put("application", "QuizPilot");
        response.put("version", "1.0.0");
        response.put("description", "Interactive Quiz Application API");
        response.put("apiVersion", "v1");
        return ResponseEntity.ok(response);
    }
    
    // Welcome endpoint (root API)
    @Operation(summary = "Welcome Message", description = "Get welcome message and available endpoints overview")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Welcome message with endpoints list")
    })
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> welcome() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Welcome to QuizPilot API");
        response.put("status", "active");
        response.put("endpoints", getAvailableEndpoints());
        return ResponseEntity.ok(response);
    }
    
    // Get available API endpoints
    @Operation(summary = "List API Endpoints", description = "Get a comprehensive list of all available API endpoints")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Endpoints list retrieved successfully")
    })
    @GetMapping("/endpoints")
    public ResponseEntity<Map<String, Object>> getEndpoints() {
        Map<String, Object> response = new HashMap<>();
        response.put("endpoints", getAvailableEndpoints());
        response.put("baseUrl", "/api");
        return ResponseEntity.ok(response);
    }
    
    // API status endpoint
    @Operation(summary = "Service Status", description = "Get current operational status of the QuizPilot service")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Service status retrieved successfully")
    })
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getStatus() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "operational");
        response.put("uptime", "Running");
        response.put("database", "connected");
        response.put("lastUpdated", String.valueOf(System.currentTimeMillis()));
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
}