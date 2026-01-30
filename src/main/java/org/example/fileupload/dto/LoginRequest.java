package org.example.fileupload.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "Telefon raqami bo'sh bo'lmasligi kerak")
    private String email;

    @NotBlank(message = "Parol bo'sh bo'lmasligi kerak")
    private String password;
}



