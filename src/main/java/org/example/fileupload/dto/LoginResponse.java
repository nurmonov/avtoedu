package org.example.fileupload.dto;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class LoginResponse {
    private String token;
    private String tokenType;
    private String username;
    private List<String> authorities;   // ← String ro'yxati
}

