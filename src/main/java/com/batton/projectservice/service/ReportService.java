package com.batton.projectservice.service;

import com.batton.projectservice.common.BaseException;
import com.batton.projectservice.domain.Belong;
import com.batton.projectservice.domain.Comment;
import com.batton.projectservice.domain.Issue;
import com.batton.projectservice.domain.Report;
import com.batton.projectservice.dto.comment.PostCommentReqDTO;
import com.batton.projectservice.dto.report.GetAddReportResDTO;
import com.batton.projectservice.dto.report.GetIssueReportResDTO;
import com.batton.projectservice.dto.report.PatchIssueReportReqDTO;
import com.batton.projectservice.enums.GradeType;
import com.batton.projectservice.enums.Status;
import com.batton.projectservice.mq.RabbitProducer;
import com.batton.projectservice.mq.dto.NoticeMessage;
import com.batton.projectservice.repository.BelongRepository;
import com.batton.projectservice.repository.CommentRepository;
import com.batton.projectservice.repository.IssueRepository;
import com.batton.projectservice.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.Optional;

import static com.batton.projectservice.common.BaseResponseStatus.*;
import static com.batton.projectservice.enums.NoticeType.COMMENT;

@RequiredArgsConstructor
@Service
public class ReportService {
    private final ReportRepository reportRepository;
    private final CommentRepository commentRepository;
    private final IssueRepository issueRepository;
    private final BelongRepository belongRepository;
    private final RabbitProducer rabbitProducer;

    /**
     * 이슈 레포트 조회 API
     */
    @Transactional
    public GetIssueReportResDTO getReport(Long memberId, Long issueId) {
        Optional<Report> report = reportRepository.findByIssueId(issueId);

        // 이슈 레포트 존재 여부
        if (report.isPresent()) {
            Optional<Belong> belong = belongRepository.findByProjectIdAndMemberId(report.get().getIssue().getProject().getId(), memberId);
            // 소속 여부
            if (belong.isPresent() && belong.get().getStatus().equals(Status.ENABLED)) {
                GetIssueReportResDTO getIssueReportResDTO = GetIssueReportResDTO.toDTO(report.get().getReportContent());

                return getIssueReportResDTO;
            } else {
                throw new BaseException(BELONG_INVALID_ID);
            }
        } else {
            throw new BaseException(ISSUE_REPORT_INVALID_ID);
        }
    }

    /**
     * 이슈 레포트 수정 API
     * */
    @Transactional
    public String patchReport(Long memberId, Long issueId, PatchIssueReportReqDTO patchIssueReportReqDTO) {
        Optional<Issue> issue = issueRepository.findById(issueId);
        Optional<Report> report = reportRepository.findByIssueId(issue.get().getId());
        Optional<Belong> belong = belongRepository.findByProjectIdAndMemberId(issue.get().getProject().getId(), memberId);

        // 소속 유저 확인
        if (belong.isPresent() && belong.get().getStatus().equals(Status.ENABLED)) {
            // 이슈 존재 여부 확인
            if (issue.isPresent()) {
                // 이슈 레포트 존재 여부 확인
                if (report.isPresent()) {
                    report.get().update(patchIssueReportReqDTO.getReportContent());
                } else {
                    throw new BaseException(ISSUE_REPORT_INVALID_ID);
                }
            } else {
                throw new BaseException(ISSUE_INVALID_ID);
            }
        } else {
            throw new BaseException(BELONG_INVALID_ID);
        }

        return "이슈 레포트 수정 성공";
    }

    /**
     * 이슈 코멘트 생성 API
     */
    @Transactional
    public String postComment(Long issueId, Long memberId, PostCommentReqDTO postCommentReqDTO) {
        Optional<Report> report = reportRepository.findByIssueId(issueId);

        // 레포트 존재 여부 확인
        if (!report.isPresent()) {
            throw new BaseException(ISSUE_REPORT_INVALID_ID);
        }
        Optional<Belong> belong = belongRepository.findByProjectIdAndMemberId(report.get().getIssue().getProject().getId(), memberId);

        // 소속 유저 확인
        if (belong.isPresent()) {
            // 권한 확인
            if (belong.get().getGrade() == GradeType.MEMBER) {
                throw new BaseException(MEMBER_NO_AUTHORITY);
            }
            Comment comment = postCommentReqDTO.toEntity(postCommentReqDTO, belong.get(), report.get());

            commentRepository.save(comment);

            rabbitProducer.sendNoticeMessage(
                    NoticeMessage.builder()
                            .projectId(belong.get().getProject().getId())
                            .noticeType(COMMENT)
                            .contentId(comment.getId())
                            .senderId(memberId)
                            .receiverId(report.get().getIssue().getBelong().getMemberId())
                            .noticeContent("[" + report.get().getIssue().getProject().getProjectTitle() + "] " + report.get().getIssue().getIssueTitle() +
                                    " 이슈 레포트에 새로운 댓글이 달렸습니다.")
                            .build());

            return "코멘트 등록되었습니다";
        } else {
            throw new BaseException(BELONG_INVALID_ID);
        }
    }

    /**
     * 추가할 이슈 레포트 조회 API
     */
    @Transactional
    public GetAddReportResDTO getAddReport(Long issueId) {
        Optional<Report> report = reportRepository.findByIssueId(issueId);

        if (report.isPresent()) {
            String issueTitle = issueRepository.findById(issueId).get().getIssueTitle();
            GetAddReportResDTO getAddReport = GetAddReportResDTO.toDTO(report.get(), issueTitle);

            return getAddReport;
        } else {
            throw new BaseException(ISSUE_REPORT_INVALID_ID);
        }
    }
}
