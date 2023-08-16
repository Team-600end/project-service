package com.batton.projectservice.service;

import com.batton.projectservice.client.MemberServiceFeignClient;
import com.batton.projectservice.common.BaseException;
import com.batton.projectservice.domain.Belong;
import com.batton.projectservice.domain.Issue;
import com.batton.projectservice.domain.Project;
import com.batton.projectservice.domain.Releases;
import com.batton.projectservice.dto.client.GetMemberResDTO;
import com.batton.projectservice.dto.project.*;
import com.batton.projectservice.enums.GradeType;
import com.batton.projectservice.enums.IssueStatus;
import com.batton.projectservice.enums.Status;
import com.batton.projectservice.mq.RabbitProducer;
import com.batton.projectservice.mq.dto.NoticeMessage;
import com.batton.projectservice.repository.BelongRepository;
import com.batton.projectservice.repository.IssueRepository;
import com.batton.projectservice.repository.ProjectRepository;
import com.batton.projectservice.repository.ReleasesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.batton.projectservice.common.BaseResponseStatus.*;
import static com.batton.projectservice.enums.NoticeType.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final BelongRepository belongRepository;
    private final ReleasesRepository releasesRepository;
    private final IssueRepository issueRepository;
    private final MemberServiceFeignClient memberServiceFeignClient;
    private final RabbitProducer rabbitProducer;

    /**
     * 프로젝트 생성 API
     * */
    @Transactional
    public PostProjectResDTO postProject(Long memberId, PostProjectReqDTO postProjectReqDTO) {
        Project project = postProjectReqDTO.toEntity(postProjectReqDTO);
        Long newProjectId = projectRepository.save(project).getId();

        // 프로젝트 생성한 사람일 경우 LEADER 권한 부여
        Belong leaderBelong = Belong.builder()
                .project(project)
                .memberId(memberId)
                .nickname(postProjectReqDTO.getNickname())
                .status(Status.ENABLED)
                .grade(GradeType.LEADER)
                .build();
        belongRepository.save(leaderBelong);

        //소속 테이블에 팀원들 추가하는 함수 불러오기
        postProjectMember(memberId, newProjectId, postProjectReqDTO.getProjectMemberList());
        PostProjectResDTO postProjectResDTO = PostProjectResDTO.toDto(newProjectId, postProjectReqDTO.getProjectKey());

        return postProjectResDTO;
    }

    /**
     * 프로젝트 고유키 중복 확인 API
     * */
    @Transactional
    public String getCheckKey(String projectKey) {
        List<Project> projectList = projectRepository.findAll();

        for (Project project : projectList) {
            if (project.getProjectKey().equals(projectKey)) {
                throw new BaseException(PROJECT_KEY_EXISTS);
            }
        }

        return "프로젝트 키가 유효합니다.";
    }

    /**
     * 프로젝트 팀원 추가 API
     * */
    @Transactional
    public String postProjectMember(Long memberId, Long projectId, List<ProjectTeamReqDTO> teamMemberList) {
        Optional<Project> project = projectRepository.findById(projectId);
        Optional<Belong> leaderBelong = belongRepository.findByProjectIdAndMemberId(projectId, memberId);

        // 프로젝트 존재 여부 확인
        if (project.isPresent()) {
            // 리더 권한 확인
            if (leaderBelong.isPresent() && leaderBelong.get().getGrade() == GradeType.LEADER) {
                // 팀원 추가
                for (ProjectTeamReqDTO projectTeamReqDTO : teamMemberList) {
                    Belong belong = ProjectTeamReqDTO.toEntity(project.get(), projectTeamReqDTO, Status.ENABLED);

                    belongRepository.save(belong);
                    log.info("프로젝트 팀원 등록 : " + memberId + "님이 프로젝트 " + projectId + "에 " + projectTeamReqDTO.getMemberId() + "님을 추가했습니다.");

                    rabbitProducer.sendNoticeMessage(
                            NoticeMessage.builder()
                                    .projectId(projectId)
                                    .noticeType(INVITE)
                                    .contentId(projectId)
                                    .senderId(memberId)
                                    .receiverId(projectTeamReqDTO.getMemberId())
                                    .noticeContent("[" + project.get().getProjectTitle() + "] " + "새로운 프로젝트 '" + project.get().getProjectTitle() + "'에 초대되었습니다.")
                                    .build());
                }
            } else {
                throw new BaseException(MEMBER_NO_AUTHORITY);
            }
        } else {
            throw new BaseException(PROJECT_INVALID_ID);
        }

        return "프로젝트 팀원 추가 성공";
    }

    /**
     * 프로젝트 수정 API
     * */
    @Transactional
    public String patchProject(Long projectId, Long memberId, PatchProjectReqDTO patchProjectReqDTO) {
        Optional<Belong> belong = belongRepository.findByProjectIdAndMemberId(projectId, memberId);

        // 소속 유저 확인
        if (belong.isPresent() && belong.get().getStatus().equals(Status.ENABLED))  {
            // 수정 권한 확인
            if (belong.get().getGrade() == GradeType.MEMBER) {
                throw new BaseException(MEMBER_NO_AUTHORITY);
            }
            Optional<Project> project = projectRepository.findById(projectId);

            // 프로젝트 존재 유무 확인
            if (project.isPresent()) {
                project.get().update(patchProjectReqDTO.getProjectTitle(), patchProjectReqDTO.getProjectContent(), patchProjectReqDTO.getProjectImage());
            } else {
                throw new BaseException(PROJECT_INVALID_ID);
            }
        } else {
            throw new BaseException(BELONG_INVALID_ID);
        }

        return "프로젝트 수정 성공";
    }

    /**
     * 프로젝트 삭제 API
     * */
    @Transactional
    public String deleteProject(Long memberId, Long projectId) {
        Optional<Belong> belong = belongRepository.findByProjectIdAndMemberId(projectId, memberId);

        // 소속 유저 확인
        if (belong.isPresent() && belong.get().getStatus().equals(Status.ENABLED)) {
            // 삭제 권한 확인
            if (belong.get().getGrade() == GradeType.MEMBER) {
                throw new BaseException(MEMBER_NO_AUTHORITY);
            } else {
                projectRepository.deleteById(projectId);
            }
        } else {
            throw new BaseException(PROJECT_INVALID_ID);
        }
        log.info("프로젝트 삭제 : " + memberId + "님이 프로젝트 " + projectId + "을 삭제했습니다.");

        return "프로젝트 삭제 성공";
    }

    /**
     * 프로젝트 상세 조회 API
     * */
    @Transactional
    public GetProjectInfoResDTO getProject(Long memberId, Long projectId) {
        Optional<Project> project = projectRepository.findById(projectId);
        Optional<Belong> belong = belongRepository.findByProjectIdAndMemberId(projectId, memberId);

        //프로젝트 존재 유뮤 확인
        if (project.isPresent()) {
            // 소속 유저 확인
            if (belong.isPresent() && belong.get().getStatus().equals(Status.ENABLED)) {
                GetProjectInfoResDTO getProjectInfoResDTO = GetProjectInfoResDTO.toDTO(project.get());

                return getProjectInfoResDTO;
            } else {
                throw new BaseException(BELONG_INVALID_ID);
            }
        } else {
            throw new BaseException(PROJECT_INVALID_ID);
        }
    }

    /**
     * 프로젝트 네비바 리스트 조회 API
     */
    @Transactional
    public List<GetProjectResDTO> getProjectListForNavbar(Long memberId) {
        List<Belong> belongList = belongRepository.findByMemberId(memberId);

        if (!belongList.isEmpty()) {
            List<GetProjectResDTO> getProjectResDTOList = new ArrayList<>();

            for (Belong belong : belongList) {
                if (belong.getStatus().equals(Status.ENABLED)) {
                    getProjectResDTOList.add(GetProjectResDTO.toDTO(belong.getProject(), belong.getGrade()));
                }
            }

            return getProjectResDTOList;
        } else {
            throw new BaseException(PROJECT_NOT_EXISTS);
        }
    }

    /**
     * 참여 중인 프로젝트 목록 조회 API
     */
    @Transactional
    public List<GetJoinedProjectListResDTO> getJoinedProjectList(Long memberId) {
        List<Belong> belongList = belongRepository.findByMemberId(memberId);
        List<GetJoinedProjectListResDTO> joinedProjectList = new ArrayList<>();

        // 소속 유저 존재 리스트 여부 검증
        if (!belongList.isEmpty()) {
            // 참여 중인 프로젝트 목록
            for (Belong belong : belongList) {
                Project project = belong.getProject();
                int todo = 0;
                int progress = 0;
                int done = 0;
                int mine = 0;
                int percentage = 0;
                double answer = 0;

                // 최신 릴리즈 노트 버전 조회
                Optional<Releases> latestReleases = releasesRepository.findFirstByProjectIdOrderByUpdatedAtDesc(project.getId());

                // 해당 멤버에게 할당된 현재 프로젝트의 이슈 리스트 조회
                List<Issue> memberIssue = issueRepository.findByBelongId(belong.getId());

                // 소속 유저 존재 여부 검증
                if (belong.getStatus().equals(Status.ENABLED)) {
                    // 해당 프로젝트의 대기, 진행, 완료 이슈 개수 조회
                    for (Issue issue : memberIssue) {
                        if (issue.getIssueStatus().equals(IssueStatus.TODO)) {
                            todo = todo + 1;
                        } else if (issue.getIssueStatus().equals(IssueStatus.PROGRESS)) {
                            progress = progress + 1;
                        } else if (issue.getIssueStatus().equals(IssueStatus.DONE)) {
                            done = done + 1;
                        }
                    }
                    // 해당 프로젝트의 진행도 계산
                    int issueNum = belong.getProject().getIssues().size();
                    if (issueNum != 0) {
                        answer = done / (double)issueNum;
                        percentage = (int)(answer * 100);
                    }
                    // 해당 프로젝트에서 해당 멤버에게 할당된 이슈 개수 조회
                    mine = memberIssue.size();
                } else {
                    throw new BaseException(BELONG_INVALID_ID);
                }
                // 프로젝트의 멤버 수 조회
                List<Belong> projectMemberList = belongRepository.findByProjectId(project.getId());
                int memberNum = 0;
                memberNum = projectMemberList.size();

                // 프로젝트 리더 조회
                Long projectLeaderId = null;
                for (Belong projectMember : projectMemberList) {
                    if (projectMember.getGrade().equals(GradeType.LEADER)) {
                        projectLeaderId = projectMember.getMemberId();
                        break;
                    }
                }
                GetMemberResDTO getMemberResDTO = memberServiceFeignClient.getMember(projectLeaderId);
                if (latestReleases.isPresent()) {
                    joinedProjectList.add(GetJoinedProjectListResDTO.toDTO(project, latestReleases.get().getVersionMajor(), latestReleases.get().getVersionMinor(), latestReleases.get().getVersionPatch(), todo, progress, done, percentage, mine, memberNum, getMemberResDTO));
                } else {
                    joinedProjectList.add(GetJoinedProjectListResDTO.toDTO(project, 0, 0, 0, todo, progress, done, percentage, mine, memberNum, getMemberResDTO));
                }
            }

            return joinedProjectList;
        } else {
            throw new BaseException(PROJECT_NOT_EXISTS);
        }
    }

    /**
     * 프로젝트 목록 조회 API
     */
    @Transactional
    public List<GetProjectListResDTO> getProjectList() {
        List<GetProjectListResDTO> getProjectListResDTOSList = new ArrayList<>();
        List<Project> projectList = new ArrayList<>();

        // 전체 조회
        projectList = projectRepository.findAll();

        for (Project project : projectList) {
            getProjectListResDTOSList.add(GetProjectListResDTO.toDTO(project));
        }

        return  getProjectListResDTOSList;
    }
}

