package org.example.fileupload.dto;



import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UploadedFileDto {
    private Integer id;
    private String storedFileName;
    private String originalFileName;
    private String contentType;
    private Long fileSize;
    private String fileUrl;
    private LocalDateTime uploadDate;
    private String text;
    private Integer userId;
    private String userFullName;
}


