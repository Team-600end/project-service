package com.batton.projectservice.domain;

import com.batton.projectservice.enums.IssueStatus;
import com.batton.projectservice.enums.IssueTag;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "issue")
public class Issue extends BaseEntity implements Comparable<Issue> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "issue_id")
    private Long id;
    private String issueTitle;
    private String issueContent;
    @Enumerated(EnumType.STRING)
    private IssueStatus issueStatus;
    @Enumerated(EnumType.STRING)
    private IssueTag issueTag;
    private int issueSeq;
    private int issueKey;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "belong_id")
    private Belong belong;
    @OneToOne(mappedBy = "issue", cascade = CascadeType.REMOVE)
    private Report report;
    private String touchList;

    @Builder
    public Issue(Long id, String issueTitle, String issueContent, IssueStatus issueStatus, IssueTag issueTag,
                 int issueSeq, int issueKey, Project project, Belong belong, String touchList) {
        this.id = id;
        this.issueTitle = issueTitle;
        this.issueContent = issueContent;
        this.issueStatus = issueStatus;
        this.issueTag = issueTag;
        this.issueSeq = issueSeq;
        this.issueKey = issueKey;
        this.project = project;
        this.belong = belong;
        this.touchList = touchList;
    }

    public void updateSeq(int issueSeq) {
        this.issueSeq = issueSeq;
    }

    public void updateIssue(int issueSeq, IssueStatus issueStatus) {
        this.issueSeq = issueSeq;
        this.issueStatus = issueStatus;
    }

    public void modifyIssue(String issueTitle, String issueContent, IssueTag issueTag, Belong belong) {
        this.issueTitle = issueTitle;
        this.issueContent = issueContent;
        this.issueTag = issueTag;
        this.belong = belong;
    }

    public void updateTouchList(String touchList) {
        this.touchList = touchList;
    }

    @Override
    public int compareTo(Issue issue) {
        return this.getUpdatedAt().compareTo(issue.getUpdatedAt());
    }
}
