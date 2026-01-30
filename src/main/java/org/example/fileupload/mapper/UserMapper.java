package org.example.fileupload.mapper;

import org.example.fileupload.dto.UserCreateRequest;
import org.example.fileupload.dto.UserResponse;
import org.example.fileupload.entity.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = UploadedFileMapper.class)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User toEntity(UserCreateRequest dto);

    UserResponse toResponse(User entity);

    List<UserResponse> toResponseList(List<User> entities);


}