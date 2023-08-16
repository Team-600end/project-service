package com.batton.projectservice.service;

import com.batton.projectservice.client.MemberServiceFeignClient;
import com.batton.projectservice.common.BaseException;
import com.batton.projectservice.domain.Belong;
import com.batton.projectservice.dto.belong.GetBelongResDTO;
import com.batton.projectservice.dto.client.GetMemberResDTO;
import com.batton.projectservice.enums.GradeType;
import com.batton.projectservice.enums.Status;
import com.batton.projectservice.mq.RabbitProducer;
import com.batton.projectservice.mq.dto.NoticeMessage;
import com.batton.projectservice.repository.BelongRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.batton.projectservice.common.BaseResponseStatus.*;
import static com.batton.projectservice.enums.NoticeType.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class BelongService {
    private final BelongRepository belongRepository;
    private final MemberServiceFeignClient memberServiceFeignClient;
    private final RabbitProducer rabbitProducer;

    /**
     * 프로젝트 팀원 권한 변경 API
     * */
    @Transactional
    public String patchGrade(Long memberId, Long projectId, Long belongId, GradeType grade) {
        Optional<Belong> myBelong = belongRepository.findByProjectIdAndMemberId(projectId, memberId);

        // 소속 확인
        if (myBelong.isPresent() && myBelong.get().getStatus().equals(Status.ENABLED)) {
            // 변경 권한 확인
            if (myBelong.get().getGrade() == GradeType.MEMBER) {
                throw new BaseException(MEMBER_NO_AUTHORITY);
            } else {
                Optional<Belong> memberBelong = belongRepository.findById(belongId);

                // 소속 확인
                if (memberBelong.isPresent() && memberBelong.get().getStatus().equals(Status.ENABLED)) {
                    memberBelong.get().update(grade);
                    rabbitProducer.sendNoticeMessage(
                            NoticeMessage.builder()
                                    .projectId(projectId)
                                    .noticeType(GRADE)
                                    .contentId(projectId)
                                    .senderId(memberId)
                                    .receiverId(memberBelong.get().getMemberId())
                                    .noticeContent("[" + memberBelong.get().getProject().getProjectTitle() + "] " + memberBelong.get().getNickname() + "님의 등급이 " + grade + " 등급으로 변경되었습니다.")
                                    .build());
                    log.info("프로젝트 팀원 권한 변경 : 유저 " + memberId + " 님이 프로젝트 " + projectId + " 의 유저 " + memberBelong.get().getMemberId() + " 님의 등급을 " + grade + " 등급으로 변경했습니다.");
                } else {
                    throw new BaseException(BELONG_INVALID_ID);
                }
            }
        } else {
            throw new BaseException(BELONG_INVALID_ID);
        }

        return "프로텍트 팀원 권한 변경 성공";
    }

    /**
     * 프로젝트 팀원 목록 조회 API
     * */
    @Transactional
    public List<GetBelongResDTO> getBelongList(Long memberId, Long projectId) {
        List<Belong> belongList = belongRepository.findBelongByProjectId(projectId);
        Optional<Belong> myBelong = belongRepository.findByProjectIdAndMemberId(projectId, memberId);
        List<GetBelongResDTO> getBelongResDTOList = new ArrayList<>();

        if(myBelong.isEmpty() || myBelong.get().getStatus().equals(Status.DISABLED)) {
            throw new BaseException(BELONG_INVALID_ID);
        }
        for (Belong belong : belongList) {
            if (belong.getStatus().equals(Status.ENABLED)) {
                GetMemberResDTO getMemberResDTO = memberServiceFeignClient.getMember(belong.getMemberId());
                getBelongResDTOList.add(GetBelongResDTO.toDTO(belong, getMemberResDTO));
            }
        }

        return getBelongResDTOList;
    }

    /**
     * 프로젝트 팀원 삭제 API
     * */
    @Transactional
    public String deleteTeamMember(Long memberId, Long belongId) {
        Optional<Belong> belong = belongRepository.findById(belongId);
        Optional<Belong> myBelong = belongRepository.findByProjectIdAndMemberId(belong.get().getProject().getId(), memberId);

        // 소속 확인
        if (belong.isPresent() && belong.get().getStatus().equals(Status.ENABLED)) {
            // 삭제 권한 확인
            if (myBelong.get().getGrade() == GradeType.MEMBER  && myBelong.get().getStatus().equals(Status.ENABLED)) {
                throw new BaseException(MEMBER_NO_AUTHORITY);
            }
            belong.get().updateStatus(Status.DISABLED);
            rabbitProducer.sendNoticeMessage(
                    NoticeMessage.builder()
                            .projectId(belong.get().getProject().getId())
                            .noticeType(EXCLUDE)
                            .contentId(belong.get().getProject().getId())
                            .senderId(memberId)
                            .receiverId(belongId)
                            .noticeContent("[" + belong.get().getProject().getProjectTitle() + "] " + belong.get().getNickname() + "님이 프로젝트에서 제외되었습니다.")
                            .build());
            log.info("프로젝트 팀원 삭제 : 유저 " + memberId + " 님이 프로젝트 " + belong.get().getProject().getId() + " 의 유저 " + belong.get().getMemberId() + " 님을 프로젝트에서 제외시켰습니다.");
        } else {
            throw new BaseException(BELONG_INVALID_ID);
        }

        return "프로젝트 멤버 삭제 성공";
    }
}
