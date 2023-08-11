package com.batton.projectservice.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "registeredIssue")
public class RegisteredIssue extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "registeredIssue_id")
    private Long id;
    private int startPosition;
    private int endPosition;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "releases_id")
    private Releases releases;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issue_id")
    private Issue issue;

    @Builder
    public RegisteredIssue(Long id, int startPosition, int endPosition, Releases releases, Issue issue) {
        this.id = id;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.releases = releases;
        this.issue = issue;
    }

    public void update(int startPosition, int endPosition, Releases releases, Issue issue) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.releases = releases;
        this.issue = issue;
    }
}