package com.quizpilot.quizpilot.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User data transfer object for API operations")
public class UserDto {
    
    @Schema(description = "Unique identifier for the user", example = "1")
    private Long id;
    
    @Schema(description = "Username for the user", example = "john_doe", required = true)
    private String username;

    @Schema(description = "Email address for the user", example = "john@gmail.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email_address;
    @Schema(description =   "Full name of the user", example = "John Doe")
    private String fullname;
    @Schema(description = "Role of the user in the system", example = "STUDENT", allowableValues = {"STUDENT", "INSTRUCTOR", "ADMIN"})
    private String userRole;
    
    // Note: We don't include password or sessionToken for security reasons
}
