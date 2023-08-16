package com.batton.projectservice.dto.release;

import com.batton.projectservice.domain.Releases;
import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
public class GetReleasesBoardResDTO {
    private Long releasesId;
    private String projectTitle;
    private String version;
    private String publishedDate;
    private List<GetReleasesIssueResDTO> issueList;

    @Builder
    public GetReleasesBoardResDTO(Long releasesId, String projectTitle, String version, String publishedDate, List<GetReleasesIssueResDTO> issueList) {
        this.releasesId = releasesId;
        this.projectTitle = projectTitle;
        this.version = version;
        this.publishedDate = publishedDate;
        this.issueList = issueList;
    }

    public static GetReleasesBoardResDTO toDTO(Releases releases, String version, String publishedDate, List<GetReleasesIssueResDTO> issueList) {
        return GetReleasesBoardResDTO.builder()
                .releasesId(releases.getId())
                .projectTitle(releases.getProject().getProjectTitle())
                .version(version)
                .publishedDate(publishedDate)
                .issueList(issueList)
                .build();
    }
}
