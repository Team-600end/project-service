package com.batton.projectservice.dto.project;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostProjectResDTO {
    private Long projectId;
    private String projectKey;

    @Builder
    public PostProjectResDTO(Long projectId, String projectKey) {
        this.projectId = projectId;
        this.projectKey = projectKey;
    }

    public static PostProjectResDTO toDto(Long projectId, String projectKey) {
        return PostProjectResDTO.builder()
                .projectId(projectId)
                .projectKey(projectKey)
                .build();
    }
}
