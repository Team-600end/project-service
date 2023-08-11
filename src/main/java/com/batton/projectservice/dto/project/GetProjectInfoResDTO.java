package com.batton.projectservice.dto.project;

import com.batton.projectservice.domain.Project;
import com.batton.projectservice.enums.GradeType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GetProjectInfoResDTO {
    private String projectTitle;
    private String projectContent;
    private String projectLogo;
    private String projectKey;

    @Builder
    public GetProjectInfoResDTO(String projectTitle, String projectContent, String projectLogo, String projectKey) {
        this.projectTitle = projectTitle;
        this.projectContent = projectContent;
        this.projectLogo = projectLogo;
        this.projectKey = projectKey;
    }

    public static GetProjectInfoResDTO toDTO(Project project) {
        return GetProjectInfoResDTO.builder()
                .projectTitle(project.getProjectTitle())
                .projectContent(project.getProjectContent())
                .projectLogo(project.getProjectImage())
                .projectKey(project.getProjectKey())
                .build();
    }
}
