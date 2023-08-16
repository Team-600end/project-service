package com.batton.projectservice.dto.issue;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostBattonTouchReqDTO {
    private String touchList;

    @Builder
    public PostBattonTouchReqDTO(String touchList) {
        this.touchList = touchList;
    }
}
