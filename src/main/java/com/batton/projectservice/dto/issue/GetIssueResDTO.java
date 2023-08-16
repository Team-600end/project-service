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
public class GetIssueResDTO {
    private Long issueId;
    private String issueTitle;
    @Enumerated(EnumType.STRING)
    private IssueTag issueTag;
    private int issueSeq;
    private int issueKey;
    @Enumerated(EnumType.STRING)
    private IssueStatus issueStatus;
    private String nickname;
    private String profileImage;

    @Builder
    public GetIssueResDTO(Long issueId, String issueTitle, IssueTag issueTag, int issueSeq, int issueKey, IssueStatus issueStatus, String nickname, String profileImage) {
        this.issueId = issueId;
        this.issueTitle = issueTitle;
        this.issueTag = issueTag;
        this.issueSeq = issueSeq;
        this.issueKey = issueKey;
        this.issueStatus = issueStatus;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    public static GetIssueResDTO toDTO(Issue issue, GetMemberResDTO getMemberResDTO) {
        return GetIssueResDTO.builder()
                .issueId(issue.getId())
                .issueTitle(issue.getIssueTitle())
                .issueTag(issue.getIssueTag())
                .issueSeq(issue.getIssueSeq())
                .issueKey(issue.getIssueKey())
                .issueStatus(issue.getIssueStatus())
                .nickname(getMemberResDTO.getNickname())
                .profileImage(getMemberResDTO.getProfileImage())
                .build();
    }
}
