package org.example.fileupload.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.example.fileupload.dto.UploadedFileDto;
import org.example.fileupload.service.FileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileUploadController {

    private final FileService fileService;

    @PostMapping(value = "/upload-multiple", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Bir nechta fayl yuklash",
            description = "Bir vaqtning o'zida bir nechta fayl (rasm, video, pdf va h.k.) yuklaydi. " +
                    "Fayllar serverda saqlanadi va metadata bazaga yoziladi."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Fayllar muvaffaqiyatli yuklandi",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = UploadedFileDto.class)))
            ),
            @ApiResponse(responseCode = "400", description = "Noto'g'ri so'rov yoki fayllar bo'sh"),
            @ApiResponse(responseCode = "500", description = "Server ichki xatosi")
    })
    public ResponseEntity<List<UploadedFileDto>> uploadMultiple(
            @RequestPart(value = "files", required = true)
            MultipartFile[] files) {

        List<UploadedFileDto> dtos = fileService.uploadMultipleFiles(files);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }
}