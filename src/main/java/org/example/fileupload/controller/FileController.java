package org.example.fileupload.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.example.fileupload.dto.UploadedFileDto;
import org.example.fileupload.service.FileService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "User uchun bir yoki bir nechta fayl yuklash",
            description = "Bir vaqtning o'zida bir nechta fayl yuklaydi. Fayllar user bilan bog'lanadi."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Fayllar muvaffaqiyatli yuklandi"),
            @ApiResponse(responseCode = "400", description = "Fayllar bo'sh yoki noto'g'ri"),
            @ApiResponse(responseCode = "404", description = "User topilmadi")
    })
    public ResponseEntity<List<UploadedFileDto>> uploadFiles(
            @RequestPart(value = "files", required = true) MultipartFile[] files,
            @RequestPart(value = "userId", required = true) Integer userId,
            @RequestPart(value = "textContent", required = false) String textContent) {

        if (files == null || files.length == 0) {
            return ResponseEntity.badRequest().body(List.of());
        }

        List<UploadedFileDto> uploadedFiles = fileService.uploadFiles(files, userId, textContent);
        return ResponseEntity.ok(uploadedFiles);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Userning yuklagan barcha fayllarini olish")
    public ResponseEntity<List<UploadedFileDto>> getUserFiles(@PathVariable Integer userId) {
        List<UploadedFileDto> files = fileService.getFilesByUserId(userId);
        return ResponseEntity.ok(files);
    }

    @GetMapping("/{fileId}")
    @Operation(summary = "Bitta fayl haqida ma'lumot olish")
    public ResponseEntity<UploadedFileDto> getFileInfo(@PathVariable Integer fileId) {
        UploadedFileDto file = fileService.getFileById(fileId);
        return ResponseEntity.ok(file);
    }
}