package com.batton.projectservice.dto.issue;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetIssueChartResDTO {
    private int toDoCnt;
    private int progressCnt;
    private int reviewCnt;
    private int completeCnt;

    @Builder
    public GetIssueChartResDTO(int todo, int progress, int review, int done) {
        this.toDoCnt = todo;
        this.progressCnt = progress;
        this.reviewCnt = review;
        this.completeCnt = done;
    }
}
