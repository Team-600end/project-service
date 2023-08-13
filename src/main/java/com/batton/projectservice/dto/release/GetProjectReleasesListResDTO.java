package com.batton.projectservice.dto.release;

import com.batton.projectservice.domain.Issue;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class GetProjectReleasesListResDTO {
    private Long releasesId;
    private String versionChanged;
    private int versionMajor;
    private int versionMinor;
    private int versionPatch;
    private String createdDate;
    private List<GetReleasesIssueResDTO> issueList;

    @Builder
    public GetProjectReleasesListResDTO(Long releasesId, String versionChanged, int versionMajor, int versionMinor, int versionPatch, String createdDate, List<GetReleasesIssueResDTO> issueList) {
        this.releasesId = releasesId;
        this.versionChanged = versionChanged;
        this.versionMajor = versionMajor;
        this.versionMinor = versionMinor;
        this.versionPatch = versionPatch;
        this.createdDate = createdDate;
        this.issueList = issueList;
    }

    public static GetProjectReleasesListResDTO toDTO(Long releasesId, String versionChanged, int versionMajor, int versionMinor, int versionPatch, String createdDate, List<GetReleasesIssueResDTO> issueList) {
        return GetProjectReleasesListResDTO.builder()
                .releasesId(releasesId)
                .versionChanged(versionChanged)
                .versionMajor(versionMajor)
                .versionMinor(versionMinor)
                .versionPatch(versionPatch)
                .createdDate(createdDate)
                .issueList(issueList)
                .build();
    }
}
