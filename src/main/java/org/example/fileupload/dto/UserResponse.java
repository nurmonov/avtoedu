package org.example.fileupload.dto;


import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


@Data
public class UserResponse {
    private Integer id;
    private String fullName;
    private String phoneNumber;
    private String email;
    private String role;
    private String status;
    private LocalDateTime createdAt;

    private List<UploadedFileDto> uploadedFiles;
}

