package com.quizpilot.quizpilot.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User data transfer object for API operations")
public class LoginDto {
    

    @Schema(description = "Email address for the user", example = "john@gmail.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email_address;
    @Schema(description = "Password for the user", example = "john1234", requiredMode=Schema.Required)
    
    // Note: We don't include password or sessionToken for security reasons
}
