package com.batton.projectservice.dto.issue;

import com.batton.projectservice.enums.IssueTag;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@NoArgsConstructor
public class PatchIssueReqDTO {
    private String issueTitle;
    private String issueContent;
    @Enumerated(EnumType.STRING)
    private IssueTag issueTag;
    private Long belongId;

    @Builder
    public PatchIssueReqDTO(String issueTitle, String issueContent, IssueTag issueTag, Long belongId) {
        this.issueTitle = issueTitle;
        this.issueContent = issueContent;
        this.issueTag = issueTag;
        this.belongId = belongId;
    }
}
