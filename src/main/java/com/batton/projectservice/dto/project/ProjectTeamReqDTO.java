package com.batton.projectservice.dto.project;

import com.batton.projectservice.domain.Belong;
import com.batton.projectservice.domain.Project;
import com.batton.projectservice.enums.GradeType;
import com.batton.projectservice.enums.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@NoArgsConstructor
@Getter
public class ProjectTeamReqDTO {
    private Long memberId;
    private String nickname;
    @Enumerated(EnumType.STRING)
    private GradeType gradeType;

    @Builder
    public ProjectTeamReqDTO(Long memberId, String nickname, GradeType gradeType) {
        this.memberId = memberId;
        this.nickname = nickname;
        this.gradeType = gradeType;
    }

    public static Belong toEntity(Project project, ProjectTeamReqDTO projectTeamReqDTO, Status status) {
        return Belong.builder()
                .project(project)
                .memberId(projectTeamReqDTO.getMemberId())
                .nickname(projectTeamReqDTO.getNickname())
                .status(status)
                .grade(projectTeamReqDTO.getGradeType())
                .build();
    }
}
