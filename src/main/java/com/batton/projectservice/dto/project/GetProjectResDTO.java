package com.batton.projectservice.dto.project;

import com.batton.projectservice.domain.Project;
import com.batton.projectservice.enums.GradeType;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
public class GetProjectResDTO {
    private Long projectId;
    private String projectTitle;
    private String projectLogo;
    private String projectKey;
    @Enumerated(EnumType.STRING)
    private GradeType memberGrade;

    @Builder
    public GetProjectResDTO(Long projectId, String projectTitle, String projectLogo, String projectKey, GradeType memberGrade) {
        this.projectId = projectId;
        this.projectTitle = projectTitle;
        this.projectLogo = projectLogo;
        this.projectKey = projectKey;
        this.memberGrade = memberGrade;
    }

    public static GetProjectResDTO toDTO(Project project, GradeType grade) {
        return GetProjectResDTO.builder()
                .projectId(project.getId())
                .projectTitle(project.getProjectTitle())
                .projectLogo(project.getProjectImage())
                .projectKey(project.getProjectKey())
                .memberGrade(grade)
                .build();
    }
}
