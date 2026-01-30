package org.example.fileupload.service;



import jakarta.transaction.Transactional;
import org.example.fileupload.dto.UploadedFileDto;
import org.example.fileupload.entity.UploadedFile;
import org.example.fileupload.repo.UploadedFileRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FileService {

    private static final String UPLOAD_DIR = "uploads/";

    private final UploadedFileRepository uploadedFileRepository;

    // Dastur ishga tushganda papka mavjudligini tekshirish (ixtiyoriy, lekin foydali)
    public FileService(UploadedFileRepository uploadedFileRepository) {
        this.uploadedFileRepository = uploadedFileRepository;
        try {
            Files.createDirectories(Paths.get(UPLOAD_DIR));
        } catch (IOException e) {
            throw new RuntimeException("Uploads papkasini yaratib bo'lmadi", e);
        }
    }

    @Transactional
    public List<UploadedFileDto> uploadMultipleFiles(MultipartFile[] files) {
        List<UploadedFileDto> result = new ArrayList<>();

        if (files == null || files.length == 0) {
            throw new IllegalArgumentException("Hech qanday fayl tanlanmagan");
        }

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue;
            }

            try {
                String originalFilename = file.getOriginalFilename();
                String extension = "";
                if (originalFilename != null && originalFilename.contains(".")) {
                    extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                }

                // Xavfsiz va noyob nom yaratish
                String storedFilename = UUID.randomUUID() + extension;

                Path targetPath = Paths.get(UPLOAD_DIR).resolve(storedFilename);
                Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

                // Entity yaratish va saqlash
                UploadedFile entity = UploadedFile.builder()
                        .originalFileName(originalFilename)
                        .storedFileName(storedFilename)
                        .filePath(targetPath.toString())
                        .contentType(file.getContentType())
                        .fileSize(file.getSize())
                        .uploadDate(LocalDateTime.now())
                        .build();

                uploadedFileRepository.save(entity);

                // DTO ga o'tkazish
                UploadedFileDto dto = UploadedFileDto.builder()
                        .id(entity.getId())
                        .originalFileName(entity.getOriginalFileName())
                        .storedFileName(entity.getStoredFileName())
                        .contentType(entity.getContentType())
                        .fileSize(entity.getFileSize())
                        .uploadDate(entity.getUploadDate())
                        .fileUrl("/api/files/download/" + entity.getStoredFileName()) // keyin download endpoint qilish mumkin
                        .build();

                result.add(dto);

            } catch (IOException e) {
                // Log qilish mumkin, lekin hozircha faqat o'tkazib yuboramiz
                System.err.println("Fayl saqlashda xato: " + file.getOriginalFilename() + " → " + e.getMessage());
            }
        }

        if (result.isEmpty()) {
            throw new RuntimeException("Hech qanday fayl muvaffaqiyatli yuklanmadi");
        }

        return result;
    }
}
