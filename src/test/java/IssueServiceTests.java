import com.batton.projectservice.client.MemberServiceFeignClient;
import com.batton.projectservice.common.BaseException;
import com.batton.projectservice.domain.*;
import com.batton.projectservice.dto.issue.*;
import com.batton.projectservice.enums.*;
import com.batton.projectservice.repository.*;
import com.batton.projectservice.service.IssueService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IssueServiceTests {
    @InjectMocks
    private IssueService issueService;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private BelongRepository belongRepository;
    @Mock
    private IssueRepository issueRepository;
    @Mock
    private ReportRepository reportRepository;

    @Test
    @DisplayName("이슈 생성 성공")
    public void testPostIssueSuccess() {
        // given
        Project project = new Project(1L,"project","test project", "image", "kea");
        Belong belong = new Belong(1L, GradeType.LEADER,1L,"harry", Status.ENABLED,project);
        List<Issue> issueList = new ArrayList<>();
        PostIssueReqDTO postIssueReqDTO = new PostIssueReqDTO(belong.getId(), project.getId(),"issue","content", IssueTag.FIXED);
        Issue issue = new Issue(1L,"issue","content",IssueStatus.TODO,IssueTag.FIXED,3,2,project,belong,"null");
        Report report = new Report(1L, "contents",issue);

        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
        when(belongRepository.findByProjectIdAndMemberId(project.getId(), belong.getMemberId())).thenReturn(Optional.of(belong));
        when(issueRepository.findByIssueStatusOrderByIssueSeq(IssueStatus.TODO)).thenReturn(issueList);
        when(belongRepository.findById(belong.getId())).thenReturn(Optional.of(belong));
        when(issueRepository.existsByProjectId(project.getId())).thenReturn(false);
        when(issueRepository.save(any(Issue.class))).thenReturn(issue);
        when(issueRepository.findById(issue.getId())).thenReturn(Optional.of(issue));
        when(reportRepository.save(any(Report.class))).thenReturn(report);

        // when
        Long issueId = issueService.postIssue(belong.getMemberId(), postIssueReqDTO);

        // then
        assertEquals(1L, issueId);
    }

    @Test
    @DisplayName("이슈 생성 시 프로젝트 예외 처리")
    public void testPostIssueInvalidProject() {
        // given
        Project project = new Project(1L,"project","test project", "image", "kea");
        Belong belong = new Belong(1L, GradeType.LEADER,1L,"harry", Status.ENABLED,project);
        PostIssueReqDTO postIssueReqDTO = new PostIssueReqDTO(belong.getId(), project.getId(),"issue","content", IssueTag.FIXED);

        when(projectRepository.findById(project.getId())).thenReturn(Optional.empty());

        // when, then
        assertThrows(BaseException.class, () -> issueService.postIssue(belong.getMemberId(), postIssueReqDTO));
    }

    @Test
    @DisplayName("이슈 생성 시 소속 예외 처리")
    public void testPostIssueInvalidBelong() {
        // given
        Project project = new Project(1L,"project","test project", "image", "kea");
        Belong belong = new Belong(1L, GradeType.LEADER,1L,"harry", Status.ENABLED,project);
        PostIssueReqDTO postIssueReqDTO = new PostIssueReqDTO(belong.getId(), project.getId(),"issue","content", IssueTag.FIXED);

        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
        when(belongRepository.findByProjectIdAndMemberId(project.getId(), belong.getMemberId())).thenReturn(Optional.empty());

        // when, then
        assertThrows(BaseException.class, () -> issueService.postIssue(belong.getMemberId(), postIssueReqDTO));
    }

    @Test
    @DisplayName("이슈 보드 상태 및 순서 변경 성공")
    public void testPatchIssueSuccess() {
        // given
        Project project = new Project(1L,"project","test project", "image", "kea");
        Belong belong = new Belong(1L, GradeType.LEADER,1L,"harry", Status.ENABLED,project);
        List<Issue> issueList = new ArrayList<>();
        Issue issue1 = new Issue(1L, "issue1","content",IssueStatus.TODO,IssueTag.CHANGED,1,2,project,belong,"null");
        Issue issue2 = new Issue(2L,"issue2","content",IssueStatus.PROGRESS,IssueTag.FIXED,1,1,project,belong,"null");
        issueList.add(issue2);

        List<Belong> belongList = new ArrayList<>();
        belongList.add(belong);

        PatchIssueBoardReqDTO patchIssueBoardReqDTO = new PatchIssueBoardReqDTO(0,IssueStatus.TODO,IssueStatus.PROGRESS, IssueCase.COMMON);

        when(issueRepository.findById(issue1.getId())).thenReturn(Optional.of(issue1));
        when(belongRepository.findByProjectIdAndMemberId(project.getId(), belong.getMemberId())).thenReturn(Optional.of(belong));
        when(issueRepository.findByIssueStatusOrderByIssueSeq(IssueStatus.PROGRESS)).thenReturn(issueList);
        when(belongRepository.findLeader(project.getId(), GradeType.LEADER)).thenReturn(belongList);

        // when
        String result = issueService.patchIssueBoard(belong.getMemberId(), issue1.getId(), patchIssueBoardReqDTO);

        // then
        assertEquals("이슈 상태 변경 되었습니다.", result);
    }

    @Test
    @DisplayName("이슈 보드 상태 및 순서 변경 시 이슈 예외 처리")
    public void testPatchIssueInvalidIssue() {
        // given
        Project project = new Project(1L,"project","test project", "image", "kea");
        Belong belong = new Belong(1L, GradeType.LEADER,1L,"harry", Status.ENABLED,project);
        Issue issue1 = new Issue(1L, "issue1","content",IssueStatus.TODO,IssueTag.CHANGED,1,2,project,belong,"null");
        PatchIssueBoardReqDTO patchIssueBoardReqDTO = new PatchIssueBoardReqDTO(0,IssueStatus.TODO,IssueStatus.PROGRESS, IssueCase.COMMON);

        when(issueRepository.findById(issue1.getId())).thenReturn(Optional.of(issue1));
        when(belongRepository.findByProjectIdAndMemberId(project.getId(), belong.getMemberId())).thenReturn(Optional.empty());

        // when, then
        assertThrows(BaseException.class, () -> issueService.patchIssueBoard(belong.getMemberId(), issue1.getId(), patchIssueBoardReqDTO));
    }

    @Test
    @DisplayName("이슈 도넛 차트 조회 성공")
    public void testGetIssueChartSuccess() {
        // given
        Project project = new Project(1L,"project","test project", "image", "kea");
        Belong belong = new Belong(1L, GradeType.LEADER,1L,"harry", Status.ENABLED,project);
        List<Issue> issueList = new ArrayList<>();
        Issue issue1 = new Issue(1L, "issue1","content",IssueStatus.TODO,IssueTag.CHANGED,1,2,project,belong,"null");
        Issue issue2 = new Issue(2L,"issue2","content",IssueStatus.PROGRESS,IssueTag.FIXED,1,1,project,belong,"null");
        issueList.add(issue1);
        issueList.add(issue2);

        when(issueRepository.findByProjectId(project.getId())).thenReturn(issueList);
        when(belongRepository.findByProjectIdAndMemberId(project.getId(), belong.getMemberId())).thenReturn(Optional.of(belong));

        // when
        GetIssueChartResDTO result = issueService.getIssueChart(belong.getMemberId(), project.getId());

        // then
        assertEquals(1, result.getToDoCnt());
        assertEquals(1, result.getProgressCnt());
        assertEquals(0, result.getReviewCnt());
        assertEquals(0, result.getCompleteCnt());
    }

    @Test
    @DisplayName("이슈 도넛 차트 조회 시 소속 예외 처리")
    public void testGetIssueChartInvalidBelong() {
        // given
        Project project = new Project(1L,"project","test project", "image", "kea");
        Belong belong = new Belong(1L, GradeType.LEADER,1L,"harry", Status.ENABLED,project);
        List<Issue> issueList = new ArrayList<>();
        Issue issue1 = new Issue(1L, "issue1","content",IssueStatus.TODO,IssueTag.CHANGED,1,2,project,belong,"null");
        Issue issue2 = new Issue(2L,"issue2","content",IssueStatus.PROGRESS,IssueTag.FIXED,1,1,project,belong,"null");
        issueList.add(issue1);
        issueList.add(issue2);

        when(issueRepository.findByProjectId(project.getId())).thenReturn(issueList);
        when(belongRepository.findByProjectIdAndMemberId(project.getId(), belong.getMemberId())).thenReturn(Optional.empty());

        // when, then
        assertThrows(BaseException.class, () -> issueService.getIssueChart(belong.getMemberId(), issue1.getId()));
    }

    @Test
    @DisplayName("개인 이슈 목록 특정 키워드 조회 성공")
    public void testGetMyIssueKeywordSuccess() {
        // given
        Project project1 = new Project(1L,"project","test project", "image", "kea");
        Belong belong1 = new Belong(1L, GradeType.LEADER,1L,"harry", Status.ENABLED,project1);
        List<Issue> issueList1 = new ArrayList<>();
        Issue issue1 = new Issue(1L, "issue1","content",IssueStatus.TODO,IssueTag.CHANGED,1,1,project1,belong1,"null");
        Issue issue2 = new Issue(2L,"issue2","content",IssueStatus.PROGRESS,IssueTag.FIXED,1,2,project1,belong1,"null");
        issueList1.add(issue1);
        issueList1.add(issue2);
        List<Belong> belongList = new ArrayList<>();
        belongList.add(belong1);

        when(belongRepository.findByMemberId(1L)).thenReturn(belongList);

        // when
        List<GetMyIssueResDTO> result = issueService.getMyIssue(belong1.getMemberId(),IssueStatus.TODO, "i");

        // then
        assertEquals(0, result.size());
    }
    @Test
    @DisplayName("개인 이슈 목록 특정 상태 조회 성공")
    public void testGetMyIssueStatusSuccess() {
        // given
        Project project1 = new Project(1L,"project","test project", "image", "kea");
        Belong belong1 = new Belong(1L, GradeType.LEADER,1L,"harry", Status.ENABLED,project1);
        List<Issue> issueList1 = new ArrayList<>();
        Issue issue1 = new Issue(1L, "issue1","content",IssueStatus.TODO,IssueTag.CHANGED,1,1,project1,belong1,"null");
        Issue issue2 = new Issue(2L,"issue2","content",IssueStatus.PROGRESS,IssueTag.FIXED,1,2,project1,belong1,"null");
        issueList1.add(issue1);
        issueList1.add(issue2);
        List<Belong> belongList = new ArrayList<>();
        belongList.add(belong1);

        when(belongRepository.findByMemberId(1L)).thenReturn(belongList);

        // when
        List<GetMyIssueResDTO> result = issueService.getMyIssue(belong1.getMemberId(),IssueStatus.TODO, "");

        // then
        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("이슈 바톤터치 성공")
    public void testPostBattonTouchSuccess() {
        // given
        Project project = new Project(1L,"project","test project", "image", "kea");
        Belong belong = new Belong(1L, GradeType.LEADER,1L,"harry", Status.ENABLED,project);
        Issue issue = new Issue(1L, "issue1","content",IssueStatus.TODO,IssueTag.CHANGED,1,2,project,belong,"null");
        PostBattonTouchReqDTO postBattonTouchReqDTO = new PostBattonTouchReqDTO("[2, 3, 4]");
        when(issueRepository.findById(issue.getId())).thenReturn(Optional.of(issue));

        // when
        String result = issueService.postBattonTouch(issue.getId(), postBattonTouchReqDTO);

        // then
        assertEquals("이슈 바톤 터치가 완료되었습니다.", result);
        assertEquals("[2, 3, 4]", issue.getTouchList());
        verify(issueRepository, times(1)).save(issue);
    }

    @Test
    @DisplayName("이슈 바톤터치 시 이슈 예외 처리")
    public void testPostBattonTouchInvalidIssue() {
        // given
        Project project = new Project(1L,"project","test project", "image", "kea");
        Belong belong = new Belong(1L, GradeType.LEADER,1L,"harry", Status.ENABLED,project);
        Issue issue = new Issue(1L, "issue1","content",IssueStatus.TODO,IssueTag.CHANGED,1,2,project,belong,"null");
        PostBattonTouchReqDTO postBattonTouchReqDTO = new PostBattonTouchReqDTO("[2, 3, 4]");

        when(issueRepository.findById(issue.getId())).thenReturn(Optional.empty());

        // when, then
        assertThrows(BaseException.class, () -> issueService.postBattonTouch(issue.getId(), postBattonTouchReqDTO));
        verify(issueRepository, never()).save(issue);
    }
}

