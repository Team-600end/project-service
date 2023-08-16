package com.batton.projectservice.dto.release;

import com.batton.projectservice.enums.PublishState;
import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
public class GetProjectReleasesListResDTO {
    private Long releasesId;
    private String versionChanged;
    private int versionMajor;
    private int versionMinor;
    private int versionPatch;
    private String createdDate;
    private PublishState isPublished;
    private List<GetReleasesIssueResDTO> issueList;

    @Builder
    public GetProjectReleasesListResDTO(Long releasesId, String versionChanged, int versionMajor, int versionMinor, int versionPatch, String createdDate, PublishState isPublished, List<GetReleasesIssueResDTO> issueList) {
        this.releasesId = releasesId;
        this.versionChanged = versionChanged;
        this.versionMajor = versionMajor;
        this.versionMinor = versionMinor;
        this.versionPatch = versionPatch;
        this.createdDate = createdDate;
        this.isPublished = isPublished;
        this.issueList = issueList;
    }

    public static GetProjectReleasesListResDTO toDTO(Long releasesId, String versionChanged, int versionMajor, int versionMinor, int versionPatch, String createdDate, PublishState publishState, List<GetReleasesIssueResDTO> issueList) {
        return GetProjectReleasesListResDTO.builder()
                .releasesId(releasesId)
                .versionChanged(versionChanged)
                .versionMajor(versionMajor)
                .versionMinor(versionMinor)
                .versionPatch(versionPatch)
                .createdDate(createdDate)
                .isPublished(publishState)
                .issueList(issueList)
                .build();
    }
}
