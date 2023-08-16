package com.batton.projectservice.dto.comment;

import com.batton.projectservice.domain.Belong;
import com.batton.projectservice.domain.Comment;
import com.batton.projectservice.domain.Report;
import com.batton.projectservice.enums.CommentType;
import lombok.Builder;
import lombok.Getter;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
public class PostCommentReqDTO {
    private String commentContent;
    @Enumerated(EnumType.STRING)
    private CommentType commentType;

    @Builder
    public PostCommentReqDTO(String commentContent, CommentType commentType) {
        this.commentContent = commentContent;
        this.commentType = commentType;
    }

    public static Comment toEntity(PostCommentReqDTO postCommentReqDTO, Belong belong, Report report) {
        return Comment.builder()
                .commentContent(postCommentReqDTO.getCommentContent())
                .commentType(postCommentReqDTO.getCommentType())
                .belong(belong)
                .report(report)
                .build();
    }
}
