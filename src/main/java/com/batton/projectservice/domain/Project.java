package com.batton.projectservice.domain;

import lombok.*;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "project")
public class Project extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long id;
    private String projectTitle;
    private String projectContent;
    private String projectImage;
    @Column(unique = true)
    private String projectKey;
    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE)
    private List<Belong> belongs = new ArrayList<>();
    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE)
    private List<Issue> issues = new ArrayList<>();
    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE)
    private List<Releases> releases = new ArrayList<>();

    @Builder
    public Project(Long id, String projectTitle, String projectContent, String projectImage, String projectKey) {
        this.id = id;
        this.projectTitle = projectTitle;
        this.projectContent = projectContent;
        this.projectImage = projectImage;
        this.projectKey = projectKey;
    }

    public void update(String projectTitle, String projectContent, String projectImage) {
        this.projectTitle = projectTitle;
        this.projectContent = projectContent;
        this.projectImage = projectImage;
    }

    public void updateInfo(String projectTitle, String projectContent) {
        this.projectTitle = projectTitle;
        this.projectContent = projectContent;
    }
}
