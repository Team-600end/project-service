package com.batton.projectservice.dto.release;

import com.batton.projectservice.domain.Project;
import com.batton.projectservice.domain.RegisteredIssue;
import com.batton.projectservice.domain.Releases;
import com.batton.projectservice.enums.PublishState;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class PostReleasesReqDTO {
    private Long projectId;
    private int versionMajor;
    private int versionMinor;
    private int versionPatch;
    private String releaseContent;
    private List<PostRegisteredIssueReqDTO> issueList;

    @Builder
    public PostReleasesReqDTO(Long projectId, int versionMajor, int versionMinor, int versionPatch, String releaseContent, List<PostRegisteredIssueReqDTO> issueList) {
        this.projectId = projectId;
        this.versionMajor = versionMajor;
        this.versionMinor = versionMinor;
        this.versionPatch = versionPatch;
        this.releaseContent = releaseContent;
        this.issueList = issueList;
    }

    public static Releases toEntity(Project project, PostReleasesReqDTO dto, PublishState publishState) {
        return Releases.builder()
                .versionMajor(dto.getVersionMajor())
                .versionMinor(dto.getVersionMinor())
                .versionPatch(dto.getVersionPatch())
                .project(project)
                .releaseContent(dto.getReleaseContent())
                .publishState(publishState)
                .build();
    }
}
