import com.batton.projectservice.common.BaseException;
import com.batton.projectservice.domain.*;
import com.batton.projectservice.dto.comment.PostCommentReqDTO;
import com.batton.projectservice.dto.report.GetIssueReportResDTO;
import com.batton.projectservice.dto.report.PatchIssueReportReqDTO;
import com.batton.projectservice.enums.*;
import com.batton.projectservice.mq.RabbitProducer;
import com.batton.projectservice.repository.BelongRepository;
import com.batton.projectservice.repository.CommentRepository;
import com.batton.projectservice.repository.IssueRepository;
import com.batton.projectservice.repository.ReportRepository;
import com.batton.projectservice.service.ReportService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReportServiceTests {
    @InjectMocks
    private ReportService reportService;
    @Mock
    private ReportRepository reportRepository;
    @Mock
    private BelongRepository belongRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private IssueRepository issueRepository;

    @Test
    @DisplayName("이슈 레포트 조회 성공")
    public void testGetReportSuccess() {
        // given
        Project project = new Project(1L,"project","test project", "image", "kea");
        Belong belong = new Belong(1L, GradeType.LEADER,1L,"harry", Status.ENABLED,project);
        Issue issue = new Issue(1L,"issue","content", IssueStatus.TODO, IssueTag.CHANGED,1,4,project,belong,null);
        Report report = new Report(1L,"contents",issue);
        when(reportRepository.findByIssueId(issue.getId())).thenReturn(Optional.of(report));
        when(belongRepository.findByProjectIdAndMemberId(issue.getProject().getId(), belong.getMemberId())).thenReturn(Optional.of(belong));

        // when
        GetIssueReportResDTO result = reportService.getReport(belong.getMemberId(), issue.getId());

        // then
        assertEquals(report.getReportContent(), result.getReportContent());
    }
    @Test
    @DisplayName("이슈 레포트 조회 시 레포트 아이디 예외 처리")
    public void testGetReportInvalidReport() {
        // given
        when(reportRepository.findByIssueId(anyLong())).thenReturn(Optional.empty());

        // when, then
        assertThrows(BaseException.class, () -> reportService.getReport(1L,1L));
    }

    @Test
    @DisplayName("이슈 레포트 수정 성공")
    public void testPatchReportSuccess() {
        // given
        Project project = new Project(1L,"project","test project", "image", "kea");
        Belong belong = new Belong(1L, GradeType.LEADER,1L,"harry", Status.ENABLED,project);
        Issue issue = new Issue(1L,"issue","content", IssueStatus.TODO, IssueTag.CHANGED,1,4,project,belong,null);
        Report report = new Report(1L,"contents",issue);
        PatchIssueReportReqDTO patchIssueReportReqDTO = new PatchIssueReportReqDTO("Updated report content");

        when(issueRepository.findById(issue.getId())).thenReturn(Optional.of(issue));
        when(reportRepository.findByIssueId(issue.getId())).thenReturn(Optional.of(report));
        when(belongRepository.findByProjectIdAndMemberId(issue.getProject().getId(), belong.getMemberId())).thenReturn(Optional.of(belong));

        // when
        String result = reportService.patchReport(belong.getMemberId(), issue.getId(), patchIssueReportReqDTO);

        // then
        assertEquals("이슈 레포트 수정 성공", result);
        assertEquals(patchIssueReportReqDTO.getReportContent(), report.getReportContent());
    }

    @Test
    @DisplayName("이슈 코멘트 생성 성공")
    public void testPostCommentSuccess() {
        // given
        Project project = new Project(1L,"project","test project", "image", "kea");
        Belong belong = new Belong(1L, GradeType.LEADER,1L,"harry", Status.ENABLED,project);
        Issue issue = new Issue(1L,"issue","content", IssueStatus.TODO, IssueTag.CHANGED,1,4,project,belong,null);
        Report report = new Report(1L,"contents",issue);
        PostCommentReqDTO postCommentReqDTO = new PostCommentReqDTO("comments", CommentType.COMMON);

        when(reportRepository.findByIssueId(issue.getId())).thenReturn(Optional.of(report));
        when(belongRepository.findByProjectIdAndMemberId(report.getIssue().getProject().getId(), belong.getMemberId())).thenReturn(Optional.of(belong));
        when(commentRepository.save(any())).thenReturn(any(Comment.class));

        // when
        String result = reportService.postComment(issue.getId(), belong.getMemberId(), postCommentReqDTO);

        // then
        assertEquals("코멘트 등록되었습니다", result);
    }

    @Test
    @DisplayName("이슈 코멘트 생성 시 권한 예외 처리")
    public void testPostCommentMemberNoAuthority() {
        // given
        Project project = new Project(1L,"project","test project", "image", "kea");
        Belong belong = new Belong(1L, GradeType.MEMBER,1L,"harry", Status.ENABLED,project);
        Issue issue = new Issue(1L,"issue","content", IssueStatus.TODO, IssueTag.CHANGED,1,4,project,belong,null);
        Report report = new Report(1L,"contents",issue);
        PostCommentReqDTO postCommentReqDTO = new PostCommentReqDTO("comments", CommentType.COMMON);

        when(reportRepository.findByIssueId(issue.getId())).thenReturn(Optional.of(report));
        when(belongRepository.findByProjectIdAndMemberId(report.getIssue().getProject().getId(), belong.getMemberId())).thenReturn(Optional.of(belong));

        assertThrows(BaseException.class, () -> reportService.postComment(issue.getId(), belong.getMemberId(), postCommentReqDTO));
    }
}

