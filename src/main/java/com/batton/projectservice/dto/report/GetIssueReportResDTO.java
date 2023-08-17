package com.batton.projectservice.dto.report;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetIssueReportResDTO {
    private String reportContent;

    @Builder
    public GetIssueReportResDTO(String reportContent) {
        this.reportContent = reportContent;
    }

    public static GetIssueReportResDTO toDTO(String reportContent) {
        return GetIssueReportResDTO.builder()
                .reportContent(reportContent)
                .build();
    }
}
