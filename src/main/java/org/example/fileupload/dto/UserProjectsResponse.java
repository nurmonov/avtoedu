package org.example.fileupload.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProjectsResponse {

    private Integer userId;
    private String fullName;

    private List<UploadedFileDto> projects;
}


