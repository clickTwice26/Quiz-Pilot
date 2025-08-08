package com.quizpilot.quizpilot.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User login credentials") 
public class LoginResponse {
    
    @Schema(description = "Status code", example = "200", requiredMode = Schema.RequiredMode.REQUIRED)
    private String statusCode;


    @Schema(description = "Email address for the user", example = "john@gmail.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email_address;
    
    @Schema(description = "Password for the user", example = "john1234", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;
}
