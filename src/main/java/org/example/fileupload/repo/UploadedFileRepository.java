package org.example.fileupload.repo;

import org.example.fileupload.entity.UploadedFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UploadedFileRepository extends JpaRepository<UploadedFile, Integer> {
    List<UploadedFile> findByUserId(Integer userId);


    Optional<UploadedFile> findByStoredFileName(String storedFileName);
}

