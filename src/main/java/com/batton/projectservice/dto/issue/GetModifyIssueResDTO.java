package com.batton.projectservice.dto.issue;

import com.batton.projectservice.enums.IssueTag;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetModifyIssueResDTO {
    private String issueTitle;
    private String issueContent;
    private IssueTag issueTag;
    private Long managerId; // 담당자 Id. 이하 담당자 정보
    private String nickname;
    private String profileImage;
    private boolean isMine; // 이슈의 담당자인지 여부

    @Builder
    public GetModifyIssueResDTO(String issueTitle, String issueContent, IssueTag issueTag, Long managerId, String nickname, String profileImage, boolean isMine) {
        this.issueTitle = issueTitle;
        this.issueContent = issueContent;
        this.issueTag = issueTag;
        this.managerId = managerId;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.isMine = isMine;
    }
}