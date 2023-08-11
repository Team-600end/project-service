package com.batton.projectservice.domain;

import com.batton.projectservice.enums.GradeType;
import com.batton.projectservice.enums.Status;
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
@Table(name = "belong")
public class Belong extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "belong_id")
    private Long id;
    @Enumerated(EnumType.STRING)
    private GradeType grade;
    private Long memberId;
    private String nickname;
    @Enumerated(EnumType.STRING)
    private Status status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;
    @OneToMany(mappedBy = "belong", cascade = CascadeType.REMOVE)
    private List<Issue> issues = new ArrayList<>();
    @OneToMany(mappedBy = "belong", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Belong(Long id, GradeType grade, Long memberId, String nickname, Status status, Project project) {
        this.id = id;
        this.grade = grade;
        this.memberId = memberId;
        this.nickname = nickname;
        this.status = status;
        this.project = project;
    }

    public void update(GradeType grade) {
        this.grade = grade;
    }

    public void updateStatus(Status status) {
        this.status = status;
    }
}
