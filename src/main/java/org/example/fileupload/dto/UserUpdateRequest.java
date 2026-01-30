package org.example.fileupload.dto;


import lombok.Data;

@Data
public class UserUpdateRequest {
    private String fullName;
    private String phoneNumber;
    private String email;
    private String newPassword;
    private String status;
}

