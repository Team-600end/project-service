package com.batton.projectservice.dto.release;

import com.batton.projectservice.domain.Releases;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class GetReleasesAllResDTO {
    private int latestVersionMajor;
    private int latestVersionMinor;
    private int latestVersionPatch;
    private List<GetProjectReleasesListResDTO> releasesList;

    @Builder
    public GetReleasesAllResDTO(int latestVersionMajor, int latestVersionMinor, int latestVersionPatch, List<GetProjectReleasesListResDTO> releasesList) {
        this.latestVersionMajor = latestVersionMajor;
        this.latestVersionMinor = latestVersionMinor;
        this.latestVersionPatch = latestVersionPatch;
        this.releasesList = releasesList;
    }

    public static GetReleasesAllResDTO toDTO(Releases releases, List<GetProjectReleasesListResDTO> releasesList) {
        return GetReleasesAllResDTO.builder()
                .latestVersionMajor(releases.getVersionMajor())
                .latestVersionMinor(releases.getVersionMinor())
                .latestVersionPatch(releases.getVersionPatch())
                .releasesList(releasesList)
                .build();
    }

    public static GetReleasesAllResDTO toDTO(int major, int minor, int patch, List<GetProjectReleasesListResDTO> releasesList) {
        return GetReleasesAllResDTO.builder()
                .latestVersionMajor(major)
                .latestVersionMinor(minor)
                .latestVersionPatch(patch)
                .releasesList(releasesList)
                .build();
    }
}
