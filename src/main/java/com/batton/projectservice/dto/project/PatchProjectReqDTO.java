package com.batton.projectservice.dto.project;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PatchProjectReqDTO {
    private String projectTitle;
    private String projectContent;
    private String projectImage;
    private String projectKey;

    @Builder
    public PatchProjectReqDTO(String projectTitle, String projectContent, String projectImage, String projectKey) {
        this.projectTitle = projectTitle;
        this.projectContent = projectContent;
        this.projectImage = projectImage;
        this.projectKey = projectKey;
    }
}
