package com.batton.projectservice.dto.issue;

import com.batton.projectservice.enums.IssueStatus;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
public class GetMyIssueReqDTO {
    @Enumerated(EnumType.STRING)
    private IssueStatus issueStatus;
    private String keyword;

    @Builder
    public GetMyIssueReqDTO(IssueStatus issueStatus, String keyword) {
        this.issueStatus = issueStatus;
        this.keyword = keyword;
    }
}
