package com.batton.projectservice.dto.project;

import com.batton.projectservice.domain.Project;
import com.batton.projectservice.dto.client.GetMemberResDTO;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GetJoinedProjectListResDTO {
    private Long projectId;
    private String projectKey;
    private String projectTitle;
    private String projectImg;
    private int versionMajor;
    private int versionMinor;
    private int versionPatch;
    private int progress;
    private int todo;
    private int done;
    private int percentage;
    private int mine;
    private int memberNum;
    private String leaderNickname;
    private String leaderProfileImg;

    @Builder
    public GetJoinedProjectListResDTO(Long projectId, String projectKey, String projectTitle, String projectImg, int versionMajor, int versionMinor, int versionPatch, int progress, int todo, int done, int percentage, int mine, int memberNum, String leaderNickname, String leaderProfileImg) {
        this.projectId = projectId;
        this.projectKey = projectKey;
        this.projectTitle = projectTitle;
        this.projectImg = projectImg;
        this.versionMajor = versionMajor;
        this.versionMinor = versionMinor;
        this.versionPatch = versionPatch;
        this.progress = progress;
        this.todo = todo;
        this.done = done;
        this.percentage = percentage;
        this.mine = mine;
        this.memberNum = memberNum;
        this.leaderNickname = leaderNickname;
        this.leaderProfileImg = leaderProfileImg;
    }

    public static GetJoinedProjectListResDTO toDTO(Project project, int versionMajor, int versionMinor, int versionPatch, int todo, int progress, int done, int percentage, int mine, int memberNum, GetMemberResDTO leader) {
        return GetJoinedProjectListResDTO.builder()
                .projectId(project.getId())
                .projectKey(project.getProjectKey())
                .projectTitle(project.getProjectTitle())
                .projectImg(project.getProjectImage())
                .versionMajor(versionMajor)
                .versionMinor(versionMinor)
                .versionPatch(versionPatch)
                .progress(progress)
                .todo(todo)
                .done(done)
                .percentage(percentage)
                .mine(mine)
                .memberNum(memberNum)
                .leaderNickname(leader.getNickname())
                .leaderProfileImg(leader.getProfileImage())
                .build();
    }

}
