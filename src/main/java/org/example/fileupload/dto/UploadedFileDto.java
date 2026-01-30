package org.example.fileupload.dto;



import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UploadedFileDto {
    private Long id;
    private String storedFileName;
    private String originalFileName;
    private String contentType;
    private Long fileSize;
    private String fileUrl;        // masalan: http://localhost:8080/files/abc.jpg
    private LocalDateTime uploadDate;
}


