package org.example.fileupload.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "uploaded_files")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadedFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String storedFileName;
    private String originalFileName;
    private String contentType;
    private Long fileSize;

    private byte[] data;

    private String filePath;

    private LocalDateTime uploadDate;


}
