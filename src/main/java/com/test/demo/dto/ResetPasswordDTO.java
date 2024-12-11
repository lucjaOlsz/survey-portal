package com.test.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetPasswordDTO {
    @NotBlank(message = "Password cannot be blank")
    private String password;

    @NotBlank(message = "Confirm password cannot be blank")
    private String confirmPassword;

}
