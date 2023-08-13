package com.batton.projectservice.dto.release;

import com.batton.projectservice.domain.Issue;
import com.batton.projectservice.domain.RegisteredIssue;
import com.batton.projectservice.dto.client.GetMemberResDTO;
import com.batton.projectservice.enums.IssueTag;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
public class GetReleasesIssueEditResDTO {
    private int startPosition;
    private int endPosition;
    private Long issueId;
    @Enumerated(EnumType.STRING)
    private IssueTag issueTag;
    private String issueTitle;
    private int issueSeq;
    private int issueKey;
    private String nickname;
    private String profileImage;


    @Builder
    public GetReleasesIssueEditResDTO(int startPosition, int endPosition, Long issueId, IssueTag issueTag, String issueTitle, int issueSeq, int issueKey, String nickname, String profileImage) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.issueId = issueId;
        this.issueTag = issueTag;
        this.issueTitle = issueTitle;
        this.issueSeq = issueSeq;
        this.issueKey = issueKey;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    public static GetReleasesIssueEditResDTO toDTO(RegisteredIssue registeredIssue, Issue issue, GetMemberResDTO getMemberResDTO) {
        return GetReleasesIssueEditResDTO.builder()
                .startPosition(registeredIssue.getStartPosition())
                .endPosition(registeredIssue.getEndPosition())
                .issueId(issue.getId())
                .issueTag(issue.getIssueTag())
                .issueTitle(issue.getIssueTitle())
                .issueSeq(issue.getIssueSeq())
                .issueKey(issue.getIssueKey())
                .nickname(getMemberResDTO.getNickname())
                .profileImage(getMemberResDTO.getProfileImage())
                .build();
    }
}
