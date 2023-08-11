package com.batton.projectservice.dto.report;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PatchIssueReportReqDTO {
    private String reportContent;

    @Builder
    public PatchIssueReportReqDTO(String reportContent) {
        this.reportContent = reportContent;
    }
}
