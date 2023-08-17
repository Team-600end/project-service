package com.batton.projectservice.dto.project;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class PatchProjectReqDTO {
    private String projectTitle;
    private String projectContent;
    private MultipartFile projectImage;
    private String projectKey;

    @Builder
    public PatchProjectReqDTO(String projectTitle, String projectContent, MultipartFile projectImage, String projectKey) {
        this.projectTitle = projectTitle;
        this.projectContent = projectContent;
        this.projectImage = projectImage;
        this.projectKey = projectKey;
    }
}
