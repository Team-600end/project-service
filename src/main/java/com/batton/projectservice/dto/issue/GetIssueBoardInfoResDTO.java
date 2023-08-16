package com.batton.projectservice.dto.issue;

import com.batton.projectservice.domain.Issue;
import com.batton.projectservice.dto.client.GetMemberResDTO;
import com.batton.projectservice.enums.IssueTag;
import lombok.Builder;
import lombok.Getter;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
public class GetIssueBoardInfoResDTO {
    private Long issueId;
    private String issueTitle;
    @Enumerated(EnumType.STRING)
    private IssueTag issueTag;
    private int issueKey;
    private String nickname;
    private String profileImage;

    @Builder
    public GetIssueBoardInfoResDTO(Long issueId, String issueTitle, IssueTag issueTag, int issueKey, String nickname, String profileImage) {
        this.issueId = issueId;
        this.issueTitle = issueTitle;
        this.issueTag = issueTag;
        this.issueKey = issueKey;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    public static GetIssueBoardInfoResDTO toDTO(Issue issue, GetMemberResDTO getMemberResDTO) {
        return GetIssueBoardInfoResDTO.builder()
                .issueId(issue.getId())
                .issueTitle(issue.getIssueTitle())
                .issueTag(issue.getIssueTag())
                .issueKey(issue.getIssueKey())
                .nickname(getMemberResDTO.getNickname())
                .profileImage(getMemberResDTO.getProfileImage())
                .build();
    }
}
