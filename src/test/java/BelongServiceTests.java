import com.batton.projectservice.client.MemberServiceFeignClient;
import com.batton.projectservice.common.BaseException;
import com.batton.projectservice.domain.Belong;
import com.batton.projectservice.domain.Project;
import com.batton.projectservice.dto.belong.GetBelongResDTO;
import com.batton.projectservice.dto.client.GetMemberResDTO;
import com.batton.projectservice.dto.project.ProjectTeamReqDTO;
import com.batton.projectservice.enums.GradeType;
import com.batton.projectservice.enums.Status;
import com.batton.projectservice.mq.RabbitProducer;
import com.batton.projectservice.repository.BelongRepository;
import com.batton.projectservice.repository.ProjectRepository;
import com.batton.projectservice.service.BelongService;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BelongServiceTests {
    @InjectMocks
    private ProjectService projectService;
    @InjectMocks
    private BelongService belongService;
    @Mock
    private BelongRepository belongRepository;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private RabbitProducer rabbitProducer;
    @Mock
    private MemberServiceFeignClient memberServiceFeignClient;


    @Test
    @DisplayName("프로젝트 팀원 권한 변경 성공")
    public void testPatchGradeSuccess() {
        // given
        Project project = new Project(1L,"project","test project", "image", "kea");
        Belong belong = new Belong(1L, GradeType.LEADER,1L,"harry", Status.ENABLED,project);
        Belong memberBelong = new Belong(2L, GradeType.MEMBER,2L,"nika", Status.ENABLED,project);
        when(belongRepository.findByProjectIdAndMemberId(project.getId(), belong.getMemberId())).thenReturn(Optional.of(belong));
        when(belongRepository.findById(memberBelong.getId())).thenReturn(Optional.of(memberBelong));

        // when
        String result = belongService.patchGrade(1L, project.getId(), memberBelong.getId(), GradeType.LEADER);

        // then
        assertEquals("프로텍트 팀원 권한 변경 성공", result);
    }

    @Test
    @DisplayName("프로젝트 팀원 권한 변경 시 권한 없음 예외 처리")
    public void testPatchGradeNoAuthority() {
        // given
        Project project = new Project(1L,"project","test project", "image", "kea");
        Belong belong = new Belong(1L, GradeType.MEMBER,1L,"harry", Status.ENABLED,project);
        when(belongRepository.findByProjectIdAndMemberId(project.getId(), belong.getMemberId())).thenReturn(Optional.of(belong));

        // when, then
        assertThrows(BaseException.class, () -> belongService.patchGrade(1L, project.getId(), 2L, GradeType.MEMBER));
    }

    @Test
    @DisplayName("프로젝트 멤버 목록 조회 성공")
    public void testGetBelongListValid() {
        // given
        Project project = new Project(1L,"project","test project", "image", "kea");
        Belong belong = new Belong(1L, GradeType.LEADER,1L,"harry", Status.ENABLED,project);
        Belong memberBelong = new Belong(2L, GradeType.MEMBER,2L,"nika", Status.ENABLED,project);

        List<Belong> belongList = new ArrayList<>();
        belongList.add(belong);
        belongList.add(memberBelong);
        GetMemberResDTO memberResDTO = new GetMemberResDTO();
        when(belongRepository.findBelongsByProjectId(project.getId(), belong.getMemberId())).thenReturn(belongList);
        when(memberServiceFeignClient.getMember(anyLong())).thenReturn(memberResDTO);

        // when
        List<GetBelongResDTO> result = belongService.getBelongList(belong.getMemberId(), project.getId());

        // then
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("프로젝트 멤버 추가 성공")
    public void testPostProjectMemberSuccess() {
        // given
        Project project = new Project(1L,"project","test project", "image", "kea");
        Belong belong = new Belong(1L, GradeType.LEADER,1L,"harry", Status.ENABLED,project);
        List<ProjectTeamReqDTO> teamMemberList = new ArrayList<>();
        teamMemberList.add(new ProjectTeamReqDTO(2L,"harry",GradeType.MEMBER));
        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
        when(belongRepository.findByProjectIdAndMemberId(project.getId(), belong.getMemberId())).thenReturn(Optional.of(belong));

        // when
        String result = projectService.postProjectMember(belong.getMemberId(), project.getId(), teamMemberList);

        // then
        assertEquals("프로젝트 팀원 추가 성공", result);
    }

    @Test
    @DisplayName("프로젝트 멤버 추가 시 권한 없음 예외 처리")
    public void testPostProjectMemberNoAuthority() {
        // given
        Project project = new Project(1L,"project","test project", "image", "kea");
        Belong belong = new Belong(1L, GradeType.MEMBER,1L,"harry", Status.ENABLED,project);
        List<ProjectTeamReqDTO> teamMemberList = new ArrayList<>();
        teamMemberList.add(new ProjectTeamReqDTO(2L,"harry",GradeType.MEMBER));
        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
        when(belongRepository.findByProjectIdAndMemberId(project.getId(), belong.getMemberId())).thenReturn(Optional.of(belong));

        // when, then
        assertThrows(BaseException.class, () -> projectService.postProjectMember(belong.getMemberId(), project.getId(), teamMemberList));
    }
}

