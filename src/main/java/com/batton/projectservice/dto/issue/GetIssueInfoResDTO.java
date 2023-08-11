package com.batton.projectservice.dto.issue;

import com.batton.projectservice.domain.Issue;
import com.batton.projectservice.dto.client.GetMemberResDTO;
import com.batton.projectservice.enums.IssueStatus;
import com.batton.projectservice.enums.IssueTag;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
public class GetIssueInfoResDTO {
    private Long issueId;
    private String issueTitle;
    private String issueContent;
    @Enumerated(EnumType.STRING)
    private IssueTag issueTag;
    @Enumerated(EnumType.STRING)
    private IssueStatus issueStatus;
    private String nickname;
    private String profileImage;
    private int issueKey;
    private String reportContent;
    private boolean isMine;

    @Builder
    public GetIssueInfoResDTO(Long issueId, String issueTitle, String issueContent, IssueTag issueTag, IssueStatus issueStatus, String nickname, String profileImage, int issueKey, String reportContent, boolean isMine) {
        this.issueId = issueId;
        this.issueTitle = issueTitle;
        this.issueContent = issueContent;
        this.issueTag = issueTag;
        this.issueStatus = issueStatus;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.issueKey = issueKey;
        this.reportContent = reportContent;
        this.isMine = isMine;
    }

    public static GetIssueInfoResDTO toDTO(Issue issue, GetMemberResDTO getMemberResDTO, String reportContent, boolean isMine) {
        return GetIssueInfoResDTO.builder()
                .issueId(issue.getId())
                .issueTitle(issue.getIssueTitle())
                .issueContent(issue.getIssueContent())
                .issueTag(issue.getIssueTag())
                .issueStatus(issue.getIssueStatus())
                .nickname(getMemberResDTO.getNickname())
                .profileImage(getMemberResDTO.getProfileImage())
                .issueKey(issue.getIssueKey())
                .reportContent(reportContent)
                .isMine(isMine)
                .build();
    }
}
