package org.example.fileupload.mapper;

import org.example.fileupload.dto.UploadedFileDto;
import org.example.fileupload.entity.UploadedFile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UploadedFileMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.fullName", target = "userFullName")
    UploadedFileDto toDto(UploadedFile entity);

    List<UploadedFileDto> toDtoList(List<UploadedFile> entities);
}


