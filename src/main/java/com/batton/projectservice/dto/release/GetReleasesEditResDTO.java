package com.batton.projectservice.dto.release;

import com.batton.projectservice.domain.Releases;
import com.batton.projectservice.enums.PublishState;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;

@Getter
public class GetReleasesEditResDTO {
    private Long id;
    private int versionMajor;
    private int versionMinor;
    private int versionPatch;
    private String publishedDate;
    private String releaseContent;
    private List<GetReleasesIssueEditResDTO> issueList;

    @Builder
public GetReleasesEditResDTO(Long id, int versionMajor, int versionMinor, int versionPatch, String publishedDate, String releaseContent, List<GetReleasesIssueEditResDTO> issueList) {
        this.id = id;
        this.versionMajor = versionMajor;
        this.versionMinor = versionMinor;
        this.versionPatch = versionPatch;
        this.publishedDate = publishedDate;
        this.releaseContent = releaseContent;
        this.issueList = issueList;
    }

    public static GetReleasesEditResDTO toDTO(Releases releases, String publishedDate, List<GetReleasesIssueEditResDTO> issueList) {
        return GetReleasesEditResDTO.builder()
                .id(releases.getId())
                .versionMajor(releases.getVersionMajor())
                .versionMinor(releases.getVersionMinor())
                .versionPatch(releases.getVersionPatch())
                .publishedDate(publishedDate)
                .releaseContent(releases.getReleaseContent())
                .issueList(issueList)
                .build();
    }
}
