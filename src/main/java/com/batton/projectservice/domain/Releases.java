package com.batton.projectservice.domain;

import com.batton.projectservice.enums.PublishState;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "releases")
public class Releases extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "releases_id")
    private Long id;
    private int versionMajor;
    private int versionMinor;
    private int versionPatch;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;
    @Column(name = "release_content", columnDefinition = "TEXT")
    private String releaseContent;
    @Enumerated(EnumType.STRING)
    private PublishState publishState;
    @OneToMany(mappedBy = "releases", cascade = CascadeType.REMOVE)
    private List<RegisteredIssue> registeredIssueList = new ArrayList<>();

    @Builder
    public Releases(Long id, int versionMajor, int versionMinor, int versionPatch, Project project, String releaseContent, List<RegisteredIssue> registeredIssueList, PublishState publishState) {
        this.id = id;
        this.versionMajor = versionMajor;
        this.versionMinor = versionMinor;
        this.versionPatch = versionPatch;
        this.project = project;
        this.releaseContent = releaseContent;
        this.registeredIssueList = registeredIssueList;
        this.publishState = publishState;
    }

    public void setPublishState(PublishState publishState) {
        this.publishState = publishState;
    }

    public void update(int versionMajor, int versionMinor, int versionPatch, String releaseContent, PublishState publishState) {
        this.versionMajor = versionMajor;
        this.versionMinor = versionMinor;
        this.versionPatch = versionPatch;
        this.releaseContent = releaseContent;
        this.publishState = publishState;
    }
}

