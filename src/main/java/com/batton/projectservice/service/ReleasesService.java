package com.batton.projectservice.service;

import com.batton.projectservice.client.MemberServiceFeignClient;
import com.batton.projectservice.common.BaseException;
import com.batton.projectservice.domain.*;
import com.batton.projectservice.dto.client.GetMemberResDTO;
import com.batton.projectservice.dto.release.*;
import com.batton.projectservice.enums.GradeType;
import com.batton.projectservice.enums.PublishState;
import com.batton.projectservice.enums.Status;
import com.batton.projectservice.repository.*;
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
import org.springframework.web.multipart.MultipartFile;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.batton.projectservice.common.BaseResponseStatus.*;
import static com.batton.projectservice.enums.NoticeType.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReleasesService {
    private final ReleasesRepository releasesRepository;
    private final ProjectRepository projectRepository;
    private final BelongRepository belongRepository;
    private final IssueRepository issueRepository;
    private final RegisteredIssueRepository registeredIssueRepository;
    private final RabbitProducer rabbitProducer;
    private final MemberServiceFeignClient memberServiceFeignClient;
    private final ObjectStorageService objectStorageService;

    /**
     * 릴리즈 생성 API
     */
    @Transactional
    public Long postReleases(Long memberId, PostReleasesReqDTO postReleasesReqDTO) {
        Optional<Project> project = projectRepository.findById(postReleasesReqDTO.getProjectId());
        Optional<Belong> belong = belongRepository.findByProjectIdAndMemberId(postReleasesReqDTO.getProjectId(), memberId);

        // 프로젝트 존재 여부 검증
        if (project.isPresent()) {
            // 프로젝트에 소속된 리더인지 검증
            if (belong.isPresent() && belong.get().getGrade() == GradeType.LEADER) {
                Releases releases = postReleasesReqDTO.toEntity(project.get(), postReleasesReqDTO, PublishState.UNPUBLISH);
                Long releaseId = releasesRepository.save(releases).getId();

                // 이슈 리스트가 존재할 경우 이슈 리스트에 릴리즈 아이디를 저장
                if (postReleasesReqDTO.getIssueList() != null) {
                    for (PostRegisteredIssueReqDTO postRegisteredIssueReqDTO : postReleasesReqDTO.getIssueList()) {
                        Optional<Issue> issue = issueRepository.findById(postRegisteredIssueReqDTO.getIssueId());
                        RegisteredIssue registeredIssue = postRegisteredIssueReqDTO.toDTO(postRegisteredIssueReqDTO, releases, issue.get());

                        registeredIssueRepository.save(registeredIssue);
                    }
                }

                return releaseId;
            } else {
                throw new BaseException(BELONG_INVALID_ID);
            }
        } else {
            throw new BaseException(PROJECT_INVALID_ID);
        }
    }

    /**
     * 릴리즈노트 발행(상태변경) API
     */
    @Transactional
    public String patchPublish(Long memberId, Long releaseId) {
        Optional<Releases> releases = releasesRepository.findById(releaseId);

        if (releases.isPresent()) {
            Optional<Belong> belong = belongRepository.findByProjectIdAndMemberId(releases.get().getProject().getId(), memberId);
            List<Belong> belongs = belongRepository.findByProjectId(releases.get().getProject().getId());
            Releases release = releases.get();

            if (belong.isEmpty()) {
                throw new BaseException(BELONG_INVALID_ID);
            } else if (belong.get().getGrade() == GradeType.MEMBER) {
                throw new BaseException(MEMBER_NO_AUTHORITY);
            }
            releases.get().setPublishState(PublishState.PUBLISH);

            // 프로젝트에 속한 모든 구성원에게 알림 전송
            for (Belong b : belongs) {
                rabbitProducer.sendNoticeMessage(
                        NoticeMessage.builder()
                                .projectId(releases.get().getProject().getId())
                                .noticeType(NEW)
                                .contentId(releaseId)
                                .senderId(memberId)
                                .receiverId(b.getMemberId())
                                .noticeContent("[" +b.getProject().getProjectTitle() + "] " + "릴리즈노트 v" +
                                        release.getVersionMajor() + "." + release.getVersionMinor() + "." + release.getVersionPatch() +
                                        " 버전이 새로 발행되었습니다.")
                                .build());
            }
        } else {
            throw new BaseException(RELEASE_NOTE_INVALID_ID);
        }
        log.info("릴리즈 발행 : 유저 " + memberId + " 님이 새 릴리즈 " + releaseId + " 을 발행했습니다.");

        return "릴리즈노트가 발행되었습니다.";
    }

    /**
     * 릴리즈 노트 수정 API
     */
    @Transactional
    public String patchReleases(Long memberId, Long releaseId, PatchReleasesReqDTO patchReleasesReqDTO) {
        Optional<Releases> releases = releasesRepository.findById(releaseId);
        Optional<Belong> belong = belongRepository.findByProjectIdAndMemberId(releases.get().getProject().getId(), memberId);

        // 소속 유저 존재 여부 & 리더 권한 검증
        if (belong.isPresent() && belong.get().getGrade().equals(GradeType.LEADER)) {
            // 릴리즈 노트 존재 여부 검증
            if (releases.isPresent()) {
                // 릴리즈 노트 수정
                releases.get().update(patchReleasesReqDTO.getVersionMajor(), patchReleasesReqDTO.getVersionMinor(), patchReleasesReqDTO.getVersionPatch(), patchReleasesReqDTO.getReleaseContent(), PublishState.UNPUBLISH);
                registeredIssueRepository.deleteAllByReleasesId(releaseId);

                // 이슈 리스트가 존재할 경우 이슈 리스트에 릴리즈 아이디를 저장
                if (patchReleasesReqDTO.getIssueList() != null) {
                    for (PostRegisteredIssueReqDTO postRegisteredIssueReqDTO : patchReleasesReqDTO.getIssueList()) {
                        Optional<Issue> issue = issueRepository.findById(postRegisteredIssueReqDTO.getIssueId());
                        RegisteredIssue registeredIssue = postRegisteredIssueReqDTO.toDTO(postRegisteredIssueReqDTO, releases.get(), issue.get());

                        registeredIssueRepository.save(registeredIssue);
                    }
                }
            } else {
                throw new BaseException(RELEASE_NOTE_INVALID_ID);
            }
        } else {
            throw new BaseException(BELONG_INVALID_ID);
        }

        return "릴리즈 노트 수정 성공";
    }


    /**
     * 릴리즈노트 삭제 API
     */
    @Transactional
    public String deleteReleases(Long memberId, Long releaseId) {
        Optional<Releases> releases = releasesRepository.findById(releaseId);

        // 릴리즈 노트 존재 여부 검증
        if (releases.isPresent()) {
            Optional<Belong> belong = belongRepository.findByProjectIdAndMemberId(releases.get().getProject().getId(), memberId);

            if (belong.isEmpty()) {
                throw new BaseException(BELONG_INVALID_ID);
            } else if (belong.get().getGrade() == GradeType.MEMBER) {
                throw new BaseException(MEMBER_NO_AUTHORITY);
            }
            releasesRepository.deleteById(releaseId);
        } else {
            throw new BaseException(RELEASE_NOTE_INVALID_ID);
        }
        log.info("릴리즈 삭제 : 유저 " + memberId + " 님이 저장된 릴리즈 " + releaseId + " 을 삭제했습니다.");

        return "릴리즈노트가 삭제되었습니다.";
    }

    /**
     * 릴리즈노트에 포함된 이슈 목록 조회 API
     */
    @Transactional
    public List<GetReleasesIssueResDTO> getReleasesIssues(Long releaseId) {
        Optional<List<RegisteredIssue>> registeredIssueList = registeredIssueRepository.findByReleasesId(releaseId);
        List<GetReleasesIssueResDTO> getReleasesIssueResDTO = new ArrayList<>();

        if (registeredIssueList.isPresent()) {
            for(RegisteredIssue registeredIssue : registeredIssueList.get()) {
                Optional<Issue> issue = issueRepository.findById(registeredIssue.getIssue().getId());

                if(issue.isPresent()){
                    getReleasesIssueResDTO.add(GetReleasesIssueResDTO.toDTO(issue.get()));
                } else {
                    throw new BaseException(ISSUE_INVALID_ID);
                }
            }
        } else {
            throw new BaseException(RELEASE_NOTE_INVALID_ID);
        }

        return getReleasesIssueResDTO;
    }

    /**
     * 릴리즈 수정용 릴리즈노트에 포함된 이슈 목록 조회 API
     */
    @Transactional
    public List<GetReleasesIssueEditResDTO> getReleasesIssuesEdit(Long releaseId) {
        Optional<List<RegisteredIssue>> registeredIssueList = registeredIssueRepository.findByReleasesId(releaseId);
        List<GetReleasesIssueEditResDTO> getReleasesIssueEditResDTO = new ArrayList<>();

        if (registeredIssueList.isPresent()) {
            for(RegisteredIssue registeredIssue : registeredIssueList.get()) {
                Optional<Issue> issue = issueRepository.findById(registeredIssue.getIssue().getId());

                if(issue.isPresent()){
                    GetMemberResDTO getMemberResDTO = memberServiceFeignClient.getMember(issue.get().getBelong().getMemberId());
                    getReleasesIssueEditResDTO.add(GetReleasesIssueEditResDTO.toDTO(registeredIssue,issue.get(), getMemberResDTO));
                } else {
                    throw new BaseException(ISSUE_INVALID_ID);
                }
            }
        } else {
            throw new BaseException(RELEASE_NOTE_INVALID_ID);
        }

        return getReleasesIssueEditResDTO;
    }

    /**
     * 릴리즈노트 상세 조회 API
     */
    @Transactional
    public GetReleasesResDTO getReleases(Long memberId, Long releaseId) {
        Optional<Releases> releases = releasesRepository.findById(releaseId);

        if (releases.isPresent()) {
            Optional<Belong> belong = belongRepository.findByProjectIdAndMemberId(releases.get().getProject().getId(), memberId);

            if(belong.isEmpty() || belong.get().getStatus().equals(Status.DISABLED)){
                throw new BaseException(BELONG_INVALID_ID);
            }
            String publishedDate = releases.get().getUpdatedAt().getYear() + ". " + releases.get().getUpdatedAt().getMonthValue() + ". " + releases.get().getUpdatedAt().getDayOfMonth();
            List<GetReleasesIssueResDTO> issueList = getReleasesIssues(releaseId);
            GetReleasesResDTO getReleasesResDTO = GetReleasesResDTO.toDTO(releases.get(), publishedDate, issueList);

            return getReleasesResDTO;
        } else {
            throw new BaseException(RELEASE_NOTE_INVALID_ID);
        }
    }

    /**
     * 릴리즈노트 수정용 상세 조회 API
     */
    @Transactional
    public GetReleasesEditResDTO getReleasesEdit(Long memberId, Long releaseId) {
        Optional<Releases> releases = releasesRepository.findById(releaseId);

        if (releases.isPresent()) {
            Optional<Belong> belong = belongRepository.findByProjectIdAndMemberId(releases.get().getProject().getId(), memberId);

            if(belong.isEmpty() || belong.get().getStatus().equals(Status.DISABLED)){
                throw new BaseException(BELONG_INVALID_ID);
            }
            String publishedDate = releases.get().getUpdatedAt().getYear() + ". " + releases.get().getUpdatedAt().getMonthValue() + ". " + releases.get().getUpdatedAt().getDayOfMonth();
            List<GetReleasesIssueEditResDTO> issueList = getReleasesIssuesEdit(releaseId);
            GetReleasesEditResDTO getReleasesEditResDTO = GetReleasesEditResDTO.toDTO(releases.get(), publishedDate, issueList);

            return getReleasesEditResDTO;
        } else {
            throw new BaseException(RELEASE_NOTE_INVALID_ID);
        }
    }

    /**
     * 프로젝트 릴리즈 노트 조회 API (+ 릴리즈 블록)
     */
    @Transactional
    public GetReleasesAllResDTO getProjectReleasesList(Long memberId, Long projectId) {
        Optional<Project> project = projectRepository.findById(projectId);
        Optional<List<Releases>> releases = releasesRepository.findByProjectIdOrderByCreatedAtAsc(projectId);
        List<GetProjectReleasesListResDTO> getProjectReleasesListResDTOList = new ArrayList<>();
        String versionChanged;
        String createdDate;
        PublishState publishState;

        // 프로젝트 존재 여부 검증
        if (project.isPresent()) {
            Optional<Belong> belong = belongRepository.findByProjectIdAndMemberId(projectId, memberId);

            if(belong.isEmpty() || belong.get().getStatus().equals(Status.DISABLED)){
                throw new BaseException(BELONG_INVALID_ID);
            }
            // 릴리즈 노트 존재 여부 검증
            if (releases.isPresent()) {
                List<Releases> releasesList = releases.get();

                for (int i = 0; i < releasesList.size(); i++) {
                    Releases release = releasesList.get(i);

                    // 버전 변경 여부 검증
                    if (i == 0) {
                        versionChanged = "Major";
                    } else {
                        Releases previousRelease = releasesList.get(i - 1);
                        if (release.getVersionMajor() != previousRelease.getVersionMajor()) {
                            versionChanged = "Major";
                        } else if (release.getVersionMinor() != previousRelease.getVersionMinor()) {
                            versionChanged = "Minor";
                        } else {
                            versionChanged = "Patch";
                        }
                    }
                    // 이슈 태그 리스트
                    List<GetReleasesIssueResDTO> issueList = getReleasesIssues(release.getId());
                    createdDate = release.getCreatedAt().getYear() + ". " + release.getCreatedAt().getMonthValue() + ". " + release.getCreatedAt().getDayOfMonth();
                    publishState = release.getPublishState();
                    GetProjectReleasesListResDTO getProjectReleasesListResDTO = GetProjectReleasesListResDTO.toDTO(release.getId(), versionChanged, release.getVersionMajor(), release.getVersionMinor(), release.getVersionPatch(), createdDate, publishState, issueList);
                    getProjectReleasesListResDTOList.add(getProjectReleasesListResDTO);
                }
                Optional<Releases> latestRelease = releasesRepository.findFirstByPublishStateOrderByUpdatedAtDesc(PublishState.PUBLISH);
                GetReleasesAllResDTO getReleasesAllResDTO;

                if(latestRelease.isPresent()) {
                    getReleasesAllResDTO = GetReleasesAllResDTO.toDTO(latestRelease.get(), getProjectReleasesListResDTOList);
                } else {
                    getReleasesAllResDTO = GetReleasesAllResDTO.toDTO(0, 0, 0, getProjectReleasesListResDTOList);
                }
                return getReleasesAllResDTO;

            } else {
                throw new BaseException(RELEASE_NOTE_INVALID_ID);
            }
        } else {
            throw new BaseException(PROJECT_INVALID_ID);
        }
    }

    /**
     * 게시판 조회 API
     */
    @Transactional
    public List<GetReleasesBoardResDTO> getRelease(Long projectId, String keyword) {
        List<Releases> releasesList;
        List<GetReleasesBoardResDTO> getReleasesBoardResDTOList = new ArrayList<>();

        if (StringUtils.isEmpty(projectId) && StringUtils.isEmpty(keyword)) {
            System.out.println("전체 조회");
            releasesList = releasesRepository.findByPublishStateOrderByUpdatedAtDesc(PublishState.PUBLISH);
        } else if (StringUtils.isEmpty(projectId) && !StringUtils.isEmpty(keyword)) {
            System.out.println("특정 릴리즈 내용 키워드");
            releasesList = releasesRepository.findByReleaseContentContainingOrderByUpdatedAtDesc(keyword);
        } else if (!StringUtils.isEmpty(projectId) && StringUtils.isEmpty(keyword)) {
            System.out.println("특정 프로젝트");
            releasesList = releasesRepository.findByPublishStateAndProjectIdOrderByUpdatedAtDesc(PublishState.PUBLISH, projectId);
        } else {
            System.out.println("특정 프로젝트와 내용 키워드");
            releasesList = releasesRepository.findByProjectIdAndReleaseContentContainingOrderByUpdatedAtDesc(projectId, keyword);
        }

        for (Releases releases : releasesList) {
            String updatedDate = releases.getUpdatedAt().getYear() + ". " + releases.getUpdatedAt().getMonthValue() + ". " + releases.getUpdatedAt().getDayOfMonth();
            String version = "v."+releases.getVersionMajor() + "." + releases.getVersionMinor() + "." + releases.getVersionPatch();
            Optional<List<RegisteredIssue>> registeredIssueList = registeredIssueRepository.findByReleasesId(releases.getId());
            List<GetReleasesIssueResDTO> issueList = new ArrayList<>();

            if (registeredIssueList.isPresent()) {
                for (RegisteredIssue registeredIssue : registeredIssueList.get()) {
                    GetReleasesIssueResDTO getReleasesIssueResDTO = GetReleasesIssueResDTO.toDTO(registeredIssue.getIssue());
                    issueList.add(getReleasesIssueResDTO);
                }
            }
            GetReleasesBoardResDTO getReleasesBoardResDTO = GetReleasesBoardResDTO.toDTO(releases, version, updatedDate, issueList);

            getReleasesBoardResDTOList.add(getReleasesBoardResDTO);
        }

        return getReleasesBoardResDTOList;
    }

    /**
     * 이미지 업로드 API
     */
    public String postImage(Long memberId, MultipartFile profileImage) {
        return objectStorageService.uploadFile(profileImage);
    }
}
