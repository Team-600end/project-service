package com.batton.projectservice.dto.issue;

import com.batton.projectservice.domain.Belong;
import com.batton.projectservice.domain.Issue;
import com.batton.projectservice.domain.Project;
import com.batton.projectservice.enums.IssueStatus;
import com.batton.projectservice.enums.IssueTag;
import lombok.Builder;
import lombok.Getter;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
public class PostIssueReqDTO {
    private Long belongId;
    private Long projectId;
    private String issueTitle;
    private String issueContent;
    @Enumerated(EnumType.STRING)
    private IssueTag issueTag;

    @Builder
    public PostIssueReqDTO(Long belongId, Long projectId, String issueTitle, String issueContent, IssueTag issueTag) {
        this.belongId = belongId;
        this.projectId = projectId;
        this.issueTitle = issueTitle;
        this.issueContent = issueContent;
        this.issueTag = issueTag;
    }

    public static Issue toEntity(Project project, Belong belong, PostIssueReqDTO dto, IssueStatus issueStatus, int issueSeq, int issueKey) {
        return Issue.builder()
                .project(project)
                .belong(belong)
                .issueTitle(dto.getIssueTitle())
                .issueContent(dto.getIssueContent())
                .issueTag(dto.getIssueTag())
                .issueStatus(issueStatus)
                .issueSeq(issueSeq)
                .issueKey(issueKey)
                .build();
    }
}
