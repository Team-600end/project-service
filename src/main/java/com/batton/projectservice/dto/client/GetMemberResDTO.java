package com.batton.projectservice.dto.client;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetMemberResDTO {
    private String nickname;
    private String profileImage;

    @Builder
    public GetMemberResDTO(String nickname, String profileImage) {
        this.nickname = nickname;
        this.profileImage = profileImage;
    }
}
