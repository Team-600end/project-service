package com.batton.projectservice.dto.report;

import com.batton.projectservice.domain.Report;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GetAddReportResDTO {
    private Long reportId;
    private String issueTitle;
    private String reportContent;

    @Builder
    public GetAddReportResDTO(Long reportId, String issueTitle, String reportContent) {
        this.reportId = reportId;
        this.issueTitle = issueTitle;
        this.reportContent = reportContent;
    }

    public static GetAddReportResDTO toDTO(Report report, String issueTitle) {
        return GetAddReportResDTO.builder()
                .reportId(report.getId())
                .issueTitle(issueTitle)
                .reportContent(report.getReportContent())
                .build();
    }
}
