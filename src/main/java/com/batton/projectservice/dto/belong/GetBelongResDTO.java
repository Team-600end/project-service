package com.batton.projectservice.dto.belong;

import com.batton.projectservice.domain.Belong;
import com.batton.projectservice.dto.client.GetMemberResDTO;
import com.batton.projectservice.enums.GradeType;
import lombok.Builder;
import lombok.Getter;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
public class GetBelongResDTO {
    private Long belongId;
    @Enumerated(EnumType.STRING)
    private GradeType grade;
    private Long memberId;
    private String nickname;
    private String profileImage;

    @Builder
    public GetBelongResDTO(Long belongId, GradeType grade, Long memberId, String nickname, String profileImage) {
        this.belongId = belongId;
        this.grade = grade;
        this.memberId = memberId;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    public static GetBelongResDTO toDTO(Belong belong, GetMemberResDTO getMemberResDTO) {
        return GetBelongResDTO.builder()
                .belongId(belong.getId())
                .grade(belong.getGrade())
                .memberId(belong.getMemberId())
                .nickname(getMemberResDTO.getNickname())
                .profileImage(getMemberResDTO.getProfileImage())
                .build();
    }
}
