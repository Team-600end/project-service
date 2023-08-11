package com.batton.projectservice.dto.report;

import com.batton.projectservice.domain.Report;
import lombok.Builder;
import lombok.Getter;

@Getter
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
