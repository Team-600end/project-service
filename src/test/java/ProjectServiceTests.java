import com.batton.projectservice.common.BaseException;
import com.batton.projectservice.domain.*;
import com.batton.projectservice.dto.project.*;
import com.batton.projectservice.enums.*;
import com.batton.projectservice.mq.RabbitProducer;
import com.batton.projectservice.repository.BelongRepository;
import com.batton.projectservice.repository.ProjectRepository;
import com.batton.projectservice.service.ProjectService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTests {
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private BelongRepository belongRepository;
    @Mock
    private RabbitProducer rabbitProducer;
    @InjectMocks
    private ProjectService projectService;

    @Test
    @DisplayName("프로젝트 생성 성공")
    public void testPostProjectSuccess() {
        // given
        List<ProjectTeamReqDTO> projectTeamReqDTOS = new ArrayList<>();
        ProjectTeamReqDTO projectTeamReqDTO = new ProjectTeamReqDTO(2L, "harry", GradeType.LEADER);
        projectTeamReqDTOS.add(projectTeamReqDTO);
        PostProjectReqDTO postProjectReqDTO = new PostProjectReqDTO("project", "kea", "test project", "image", "nika", projectTeamReqDTOS);
        Project project = new Project(1L,"project","test project", "image", "kea");
        Belong belong = new Belong(1L, GradeType.LEADER,2L,"harry",Status.ENABLED,project);

        when(projectRepository.save(any(Project.class))).thenReturn(project);
        when(projectRepository.findById(anyLong())).thenReturn(Optional.of(project));
        when(belongRepository.findByProjectIdAndMemberId(anyLong(), anyLong())).thenReturn(Optional.of(belong));
        when(rabbitProducer.sendNoticeMessage(any())).thenReturn("알림 데이터 전송 완료");

        // when
        PostProjectResDTO result = projectService.postProject(1L, postProjectReqDTO);

        // given
        assertEquals(1L, result.getProjectId());
        verify(projectRepository, times(1)).save(any(Project.class));
        verify(belongRepository, times(2)).save(any(Belong.class));
    }

    @Test
    @DisplayName("프로젝트 고유키 유효 성공")
    public void testGetCheckKeySuccess() {
        // given
        List<Project> projectList = new ArrayList<>();
        when(projectRepository.findAll()).thenReturn(projectList);

        // when
        String result = projectService.getCheckKey("kea");

        assertEquals("프로젝트 키가 유효합니다.", result);
    }

    @Test
    @DisplayName("프로젝트 고유키 중복 예외 처리")
    public void testGetCheckKeyExists() {
        // given
        List<Project> projectList = new ArrayList<>();
        projectList.add(new Project(1L,"project","content","image","kea"));
        when(projectRepository.findAll()).thenReturn(projectList);

        // when, then
        assertThrows(BaseException.class, () -> projectService.getCheckKey("kea"));
    }

    @Test
    @DisplayName("프로젝트 수정 성공")
    public void testPatchProjectSuccess() {
        // given
        PatchProjectReqDTO patchProjectReqDTO = new PatchProjectReqDTO("project","test project","image","kea");
        Project project = new Project(1L,"project","test project", "image", "kea");
        Belong belong = new Belong(1L, GradeType.LEADER,1L,"harry",Status.ENABLED,project);
        when(belongRepository.findByProjectIdAndMemberId(project.getId(), belong.getMemberId())).thenReturn(Optional.of(belong));
        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));

        // when
        String result = projectService.patchProject(project.getId(), belong.getMemberId(), patchProjectReqDTO);

        // then
        assertEquals("프로젝트 수정 성공", result);
    }

    @Test
    @DisplayName("프로젝트 수정 시 권한 없음 예외 처리")
    public void testPatchProjectNoAuthority() {
        // given
        PatchProjectReqDTO patchProjectReqDTO = new PatchProjectReqDTO("project","test project","image","kea");
        Project project = new Project(1L,"project","test project", "image", "kea");
        Belong belong = new Belong(1L, GradeType.MEMBER,1L,"harry",Status.ENABLED,project);
        when(belongRepository.findByProjectIdAndMemberId(project.getId(), belong.getMemberId())).thenReturn(Optional.of(belong));

        // when, then
        assertThrows(BaseException.class, () -> projectService.patchProject(project.getId(), belong.getMemberId(), patchProjectReqDTO));
    }

    @Test
    @DisplayName("프로젝트 수정 시 프로젝트 아이디 예외 처리")
    public void testPatchProjectInvalidProject() {
        // given
        PatchProjectReqDTO patchProjectReqDTO = new PatchProjectReqDTO("project","test project","image","kea");
        Project project = new Project(1L,"project","test project", "image", "kea");
        Belong belong = new Belong(1L, GradeType.LEADER,1L,"harry",Status.ENABLED,project);

        when(belongRepository.findByProjectIdAndMemberId(project.getId(), belong.getMemberId())).thenReturn(Optional.of(belong));
        when(projectRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when, then
        assertThrows(BaseException.class, () -> projectService.patchProject(project.getId(), belong.getMemberId(), patchProjectReqDTO));
    }

    @Test
    @DisplayName("프로젝트 상세 조회 성공")
    public void testGetProjectSuccess() {
        // given
        Project project = new Project(1L,"project","test project", "image", "kea");
        Belong belong = new Belong(1L, GradeType.LEADER,1L,"harry",Status.ENABLED,project);
        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
        when(belongRepository.findByProjectIdAndMemberId(project.getId(), belong.getMemberId())).thenReturn(Optional.of(belong));

        // when
        GetProjectInfoResDTO result = projectService.getProject(belong.getMemberId(), project.getId());

        // then
        assertNotNull(result);
        assertEquals(project.getProjectTitle(), result.getProjectTitle());
    }

    @Test
    @DisplayName("프로젝트 조회 시 소속 예외 처리")
    public void testGetProjectInvalidBelong() {
        // given
        Project project = new Project(1L,"project","test project", "image", "kea");
        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
        when(belongRepository.findByProjectIdAndMemberId(anyLong(), anyLong())).thenReturn(Optional.empty());

        // when, then
        assertThrows(BaseException.class, () -> projectService.getProject(anyLong(), project.getId()));
    }

    @Test
    @DisplayName("프로젝트 목록 전체 검색 성공")
    public void testGetProjectListAll() {
        // given
        Project project1 = new Project(1L,"project","test project", "image", "kea");
        Project project2 = new Project(2L,"project2","test project2", "image", "dk");
        List<Project> projectList = new ArrayList<>();
        projectList.add(project1);
        projectList.add(project2);
        when(projectRepository.findAll()).thenReturn(projectList);

        // when
        List<GetProjectListResDTO> result = projectService.getProjectList();

        // given
        assertEquals(projectList.size(), result.size());
    }
}

  