package com.batton.projectservice.dto.comment;

import com.batton.projectservice.domain.Comment;
import com.batton.projectservice.dto.client.GetMemberResDTO;
import com.batton.projectservice.enums.CommentType;
import com.batton.projectservice.enums.GradeType;
import lombok.Builder;
import lombok.Getter;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
public class GetCommentResDTO {
    private String commentContent;
    @Enumerated(EnumType.STRING)
    private CommentType commentType;
    private String createdDate;
    private String nickname;
    private String profileImage;
    @Enumerated(EnumType.STRING)
    private GradeType memberGrade;

    @Builder
    public GetCommentResDTO(String commentContent, CommentType commentType, String createdDate, String nickname, String profileImage, GradeType memberGrade) {
        this.commentContent = commentContent;
        this.commentType = commentType;
        this.createdDate = createdDate;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.memberGrade = memberGrade;
    }

    public static GetCommentResDTO toDTO(Comment comment, GetMemberResDTO getMemberResDTO, String createdDate, GradeType memberGrade) {
        return GetCommentResDTO.builder()
                .commentContent(comment.getCommentContent())
                .commentType(comment.getCommentType())
                .createdDate(createdDate)
                .nickname(getMemberResDTO.getNickname())
                .profileImage(getMemberResDTO.getProfileImage())
                .memberGrade(memberGrade)
                .build();
    }
}
