package com.batton.projectservice.dto.release;

import com.batton.projectservice.domain.Releases;
import com.batton.projectservice.enums.PublishState;
import lombok.Builder;
import lombok.Getter;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;

@Getter
public class GetReleasesResDTO {
    private Long id;
    private int versionMajor;
    private int versionMinor;
    private int versionPatch;
    private String publishedDate;
    private String releaseContent;
    @Enumerated(EnumType.STRING)
    private PublishState isPublished;
    private List<GetReleasesIssueResDTO> issueList;

    @Builder
    public GetReleasesResDTO(Long id, int versionMajor, int versionMinor, int versionPatch, String publishedDate, String releaseContent, PublishState isPublished, List<GetReleasesIssueResDTO> issueList) {
        this.id = id;
        this.versionMajor = versionMajor;
        this.versionMinor = versionMinor;
        this.versionPatch = versionPatch;
        this.publishedDate = publishedDate;
        this.releaseContent = releaseContent;
        this.isPublished = isPublished;
        this.issueList = issueList;
    }

    public static GetReleasesResDTO toDTO(Releases releases, String publishedDate, List<GetReleasesIssueResDTO> issueList) {
        return GetReleasesResDTO.builder()
                .id(releases.getId())
                .versionMajor(releases.getVersionMajor())
                .versionMinor(releases.getVersionMinor())
                .versionPatch(releases.getVersionPatch())
                .publishedDate(publishedDate)
                .releaseContent(releases.getReleaseContent())
                .isPublished(releases.getPublishState())
                .issueList(issueList)
                .build();
    }
}
