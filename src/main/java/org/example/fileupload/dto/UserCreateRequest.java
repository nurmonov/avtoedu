package org.example.fileupload.dto;


import lombok.Data;

@Data
public class UserCreateRequest {

    private String fullName;
    private String phoneNumber;
    private String email;
    private String password;
    private String role;
}
