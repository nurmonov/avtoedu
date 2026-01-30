package org.example.fileupload.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.fileupload.dto.UploadedFileDto;
import org.example.fileupload.entity.UploadedFile;
import org.example.fileupload.entity.User;
import org.example.fileupload.mapper.UploadedFileMapper;
import org.example.fileupload.repo.UploadedFileRepository;
import org.example.fileupload.repo.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private static final String UPLOAD_DIR = "uploads/";

    private final UploadedFileRepository fileRepository;
    private final UserRepository userRepository;
    private final UploadedFileMapper mapper;

    @Value("${app.file.base-url:/uploads/}")
    private String fileBaseUrl;



    @Transactional
    public List<UploadedFileDto> uploadFiles(MultipartFile[] files, Integer userId, String textContent) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User topilmadi: ID = " + userId));

        List<UploadedFileDto> result = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file == null || file.isEmpty()) {
                log.warn("Bo'sh fayl o'tkazib yuborildi");
                continue;
            }

            try {
                String originalName = file.getOriginalFilename();
                String ext = "";
                if (originalName != null && originalName.contains(".")) {
                    ext = originalName.substring(originalName.lastIndexOf("."));
                }

                String storedName = UUID.randomUUID() + ext;
                Path target = Paths.get(UPLOAD_DIR, storedName);

                Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

                UploadedFile entity = UploadedFile.builder()
                        .originalFileName(originalName)
                        .storedFileName(storedName)
                        .filePath(target.toString())
                        .contentType(file.getContentType())
                        .fileSize(file.getSize())
                        .textContent(textContent)
                        .uploadDate(LocalDateTime.now())
                        .user(user)
                        .build();

                fileRepository.save(entity);
                result.add(mapper.toDto(entity));

                log.info("Fayl yuklandi: {} → userId: {}", storedName, userId);

            } catch (IOException e) {
                log.error("Fayl saqlashda xato: {} → {}", file.getOriginalFilename(), e.getMessage());
            }
        }

        if (result.isEmpty()) {
            throw new IllegalStateException("Hech qanday fayl muvaffaqiyatli yuklanmadi");
        }

        return result;
    }

    @Transactional(readOnly = true)
    public List<UploadedFileDto> getFilesByUserId(Integer userId) {
        return mapper.toDtoList(fileRepository.findByUserId(userId));
    }

    @Transactional(readOnly = true)
    public UploadedFileDto getFileById(Integer fileId) {
        UploadedFile file = fileRepository.findById(fileId)
                .orElseThrow(() -> new IllegalArgumentException("Fayl topilmadi: ID = " + fileId));
        return mapper.toDto(file);
    }
}