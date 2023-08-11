package com.batton.projectservice.dto.issue;

import com.batton.projectservice.domain.Issue;
import com.batton.projectservice.dto.client.GetMemberResDTO;
import com.batton.projectservice.dto.comment.GetCommentResDTO;
import com.batton.projectservice.enums.IssueStatus;
import com.batton.projectservice.enums.IssueTag;
import lombok.Builder;
import lombok.Getter;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.ArrayList;
import java.util.List;

@Getter
public class GetIssueReportResDTO {
    private int issueKey;
    private String issueTitle;
    private String issueContent;
    @Enumerated(EnumType.STRING)
    private IssueTag issueTag;
    @Enumerated(EnumType.STRING)
    private IssueStatus issueStatus;
    private String updatedDate;
    private String nickname;
    private String profileImage;
    private String reportContent;
    private List<GetCommentResDTO> commentList = new ArrayList<>();

    @Builder
    public GetIssueReportResDTO(int issueKey, String issueTitle, String issueContent, IssueTag issueTag, IssueStatus issueStatus, String updatedDate, String nickname, String profileImage, String reportContent, List<GetCommentResDTO> commentList) {
        this.issueKey = issueKey;
        this.issueTitle = issueTitle;
        this.issueContent = issueContent;
        this.issueTag = issueTag;
        this.issueStatus = issueStatus;
        this.updatedDate = updatedDate;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.reportContent = reportContent;
        this.commentList = commentList;
    }

    public static GetIssueReportResDTO toDTO(Issue issue, String updatedDate, GetMemberResDTO getMemberInfoResDTO, String reportContent, List<GetCommentResDTO> commentList) {
        return GetIssueReportResDTO.builder()
                .issueKey(issue.getIssueKey())
                .issueTitle(issue.getIssueTitle())
                .issueContent(issue.getIssueContent())
                .issueTag(issue.getIssueTag())
                .issueStatus(issue.getIssueStatus())
                .updatedDate(updatedDate)
                .nickname(getMemberInfoResDTO.getNickname())
                .profileImage(getMemberInfoResDTO.getProfileImage())
                .reportContent(reportContent)
                .commentList(commentList)
                .build();
    }

}
