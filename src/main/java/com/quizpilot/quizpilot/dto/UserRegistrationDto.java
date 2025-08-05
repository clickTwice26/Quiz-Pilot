package com.quizpilot.quizpilot.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User registration request object")
public class UserRegistrationDto {
    
    @Schema(description = "Username for the new user", example = "john_doe", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;
    
    @Schema(description = "Email address for the new user", example = "john@gmail.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String emailAddress;
    
    @Schema(description = "Password for the new user", example = "securePassword123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;
    
    @Schema(description = "Full name of the user", example = "John Doe", requiredMode = Schema.RequiredMode.REQUIRED)
    private String fullname;
    

}
