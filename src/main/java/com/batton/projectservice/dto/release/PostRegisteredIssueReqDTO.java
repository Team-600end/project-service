package com.batton.projectservice.dto.release;

import com.batton.projectservice.domain.Issue;
import com.batton.projectservice.domain.RegisteredIssue;
import com.batton.projectservice.domain.Releases;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostRegisteredIssueReqDTO {
    private int startPosisition;
    private int endPosition;
    private Long issueId;
    @Builder
    public PostRegisteredIssueReqDTO(int startPosisition, int endPosition, Long issueId) {
        this.startPosisition = startPosisition;
        this.endPosition = endPosition;
        this.issueId = issueId;
    }

    public static RegisteredIssue toDTO(PostRegisteredIssueReqDTO postRegisteredIssueReqDTO, Releases releases, Issue issue) {
        return RegisteredIssue.builder()
                .startPosition(postRegisteredIssueReqDTO.getStartPosisition())
                .endPosition(postRegisteredIssueReqDTO.getEndPosition())
                .releases(releases)
                .issue(issue)
                .build();
    }
}
