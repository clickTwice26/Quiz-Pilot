package com.quizpilot.quizpilot.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Authenticated user response with endpoint data")
public class AuthenticatedResponse<T> {
    
    @Schema(description = "Status code", example = "200")
    private String status;
    
    @Schema(description = "Response message", example = "Success")
    private String message;
    
    @Schema(description = "Authenticated user information")
    private UserDto user;
    
    @Schema(description = "Endpoint response data")
    private T data;
}
