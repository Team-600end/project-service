package com.batton.projectservice.dto.release;

import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
public class PatchReleasesReqDTO {
    private int versionMajor;
    private int versionMinor;
    private int versionPatch;
    private String releaseContent;
    private List<PostRegisteredIssueReqDTO> issueList;

    @Builder
    public PatchReleasesReqDTO(int versionMajor, int versionMinor, int versionPatch, String releaseContent, List<PostRegisteredIssueReqDTO> issueList) {
        this.versionMajor = versionMajor;
        this.versionMinor = versionMinor;
        this.versionPatch = versionPatch;
        this.releaseContent = releaseContent;
        this.issueList = issueList;
    }
}
