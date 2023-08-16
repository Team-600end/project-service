package com.batton.projectservice.controller;

import com.batton.projectservice.common.BaseResponse;
import com.batton.projectservice.dto.project.*;
import com.batton.projectservice.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/projects")
public class ProjectController {
    private final ProjectService projectService;

    /**
     * 프로젝트 생성 API
     * @param memberId 프로젝트 생성하는 유저 아이디
     * @param postProjectReqDTO 프로젝트 생성 요청 바디에 포함될 PostProjectReqDTO
     * @return PostProjectResDTO
     * */
    @PostMapping
    @Operation(summary = "프로젝트 생성")
    private BaseResponse<PostProjectResDTO> postProject(@RequestHeader Long memberId,
                                                        @RequestBody PostProjectReqDTO postProjectReqDTO) {
        PostProjectResDTO postProjectRes = projectService.postProject(memberId, postProjectReqDTO);

        return new BaseResponse<>(postProjectRes);
    }

    /**
     * 프로젝트 고유키 중복 확인 API
     * @param projectKey 확인할 프로젝트 고유키
     */
    @GetMapping("/project-key/{projectKey}")
    @Operation(summary = "프로젝트 고유키 중복 확인")
    @ApiResponses({
            @ApiResponse(responseCode = "708", description = "중복된 프로젝트 키입니다.")
    })
    private BaseResponse<String> getCheckKey(@PathVariable("projectKey") String projectKey) {
        String check = projectService.getCheckKey(projectKey);

        return new BaseResponse<>(check);
    }

    /**
     * 프로젝트 수정 API
     * @param memberId 프로젝트 수정 작업을하는 유저 아이디
     * @param projectId 수정할 프로젝트 아이디
     * @param patchProjectReqDTO 프로젝트 수정 요청 PatchProjectReqDTO
     * @return String
     */
    @PatchMapping("/{projectId}")
    @Operation(summary = "프로젝트 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "700", description = "유저에게 해당 권한이 없습니다."),
            @ApiResponse(responseCode = "701", description = "프로젝트 아이디 값을 확인해주세요."),
            @ApiResponse(responseCode = "703", description = "소속 아이디 값을 확인해주세요.")
    })
    private BaseResponse<String> patchProject(@RequestHeader Long memberId,
                                              @PathVariable("projectId") Long projectId,
                                              @RequestBody PatchProjectReqDTO patchProjectReqDTO) {
        String patchProjectRes = projectService.patchProject(memberId, projectId, patchProjectReqDTO);

        return new BaseResponse<>(patchProjectRes);
    }

    /**
     * 프로젝트 삭제 API
     * @param memberId 프로젝트 삭제 작업을하는 유저 아이디
     * @param projectId 삭제할 프로젝트 아이디
     * @return String
     */
    @DeleteMapping("/{projectId}")
    @Operation(summary = "프로젝트 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "700", description = "유저에게 해당 권한이 없습니다."),
            @ApiResponse(responseCode = "701", description = "프로젝트 아이디 값을 확인해주세요."),
            @ApiResponse(responseCode = "703", description = "소속 아이디 값을 확인해주세요.")
    })
    private BaseResponse<String> deleteProject(@RequestHeader Long memberId,
                                               @PathVariable("projectId") Long projectId) {
        String deleteProjectRes = projectService.deleteProject(memberId, projectId);

        return new BaseResponse<>(deleteProjectRes);
    }

    /**
     * 프로젝트 상세 조회 API
     * @param memberId 프로젝트 조회 작업을하는 유저 아이디
     * @param projectId 조회할 프로젝트 아이디
     * @return GetProjectInfoResDTO
     */
    @GetMapping("/{projectId}")
    @Operation(summary = "프로젝트 상세 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "701", description = "프로젝트 아이디 값을 확인해주세요."),
            @ApiResponse(responseCode = "703", description = "소속 아이디 값을 확인해주세요.")
    })
    private BaseResponse<GetProjectInfoResDTO> getProject(@RequestHeader Long memberId,
                                                          @PathVariable("projectId") Long projectId) {
        GetProjectInfoResDTO getProjectInfoResDTO = projectService.getProject(memberId, projectId);

        return new BaseResponse<>(getProjectInfoResDTO);
    }

    /**
     * 프로젝트 네비바 리스트 조회 API
     * @param memberId 프로젝트 조회 작업을하는 유저 아이디
     * @return List<GetProjectResDTO>
     */
    @GetMapping("/navbar")
    @Operation(summary = "프로젝트 네비바 리스트 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "707", description = "참여한 프로젝트가 없습니다.")
    })
    private BaseResponse<List<GetProjectResDTO>> getProjectListForNavbar(@RequestHeader Long memberId) {
        List<GetProjectResDTO> getProjectResDTOList = projectService.getProjectListForNavbar(memberId);

        return new BaseResponse<>(getProjectResDTOList);
    }

    /**
     * 참여 중인 프로젝트 리스트 조회 API
     * @param memberId 프로젝트 조회 작업을하는 유저 아이디
     * @retur List<GetJoinedProjectListResDTO>
     */
    @GetMapping("/joinedList")
    @Operation(summary = "참여 중인 프로젝트 리스트 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "707", description = "참여한 프로젝트가 없습니다.")
    })
    private BaseResponse<List<GetJoinedProjectListResDTO>> getJoinedProjectList(@RequestHeader Long memberId) {
        List<GetJoinedProjectListResDTO> getJoinedProjectListResDTOList = projectService.getJoinedProjectList(memberId);

        return new BaseResponse<>(getJoinedProjectListResDTOList);
    }

    /**
     * 프로젝트 목록  조회 API
     * @return List<GetProjectListResDTO>
     */
    @GetMapping("/list")
    @Operation(summary = "프로젝트 목록 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "600", description = "유저 아이디 값을 확인해주세요.")
    })
    private BaseResponse<List<GetProjectListResDTO>> getProjectList() {
        List<GetProjectListResDTO> getProjectListResDTOList = projectService.getProjectList();

        return new BaseResponse<>(getProjectListResDTOList);
    }
}
