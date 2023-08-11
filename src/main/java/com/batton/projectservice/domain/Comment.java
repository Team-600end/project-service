package com.batton.projectservice.domain;

import com.batton.projectservice.enums.CommentType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comment")
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;
    private String commentContent;
    @Enumerated(EnumType.STRING)
    private CommentType commentType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id")
    private Report report;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "belong_id")
    private Belong belong;

    @Builder
    public Comment(Long id, String commentContent, CommentType commentType, Report report, Belong belong) {
        this.id = id;
        this.commentContent = commentContent;
        this.commentType = commentType;
        this.report = report;
        this.belong = belong;
    }
}
