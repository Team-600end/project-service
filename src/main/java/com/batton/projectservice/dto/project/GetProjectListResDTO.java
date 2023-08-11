package com.batton.projectservice.dto.project;

import com.batton.projectservice.domain.Project;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GetProjectListResDTO {
    private Long projectId;
    private String projectTitle;
    private String projectLogo;

    @Builder
    public GetProjectListResDTO(Long projectId, String projectTitle, String projectLogo) {
        this.projectId = projectId;
        this.projectTitle = projectTitle;
        this.projectLogo = projectLogo;
    }

    public static GetProjectListResDTO toDTO(Project project) {
        return GetProjectListResDTO.builder()
                .projectId(project.getId())
                .projectTitle(project.getProjectTitle())
                .projectLogo(project.getProjectImage())
                .build();
    }
}
