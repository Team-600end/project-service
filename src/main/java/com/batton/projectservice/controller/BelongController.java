package com.batton.projectservice.controller;

import com.batton.projectservice.common.BaseResponse;
import com.batton.projectservice.dto.project.ProjectTeamReqDTO;
import com.batton.projectservice.dto.belong.GetBelongResDTO;
import com.batton.projectservice.enums.GradeType;
import com.batton.projectservice.service.BelongService;
import com.batton.projectservice.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/belongs")
public class BelongController {
    private final ProjectService projectService;
    private final BelongService belongService;

    /**
     * 프로젝트 멤버 추가를 위한 API
     * @param projectId 프로젝트 아이디
     * @param memberId 추가하는 작업을 하는 멤버의 아이디
     * @param projectTeamReqDTOList 요청 바디에 포함될 ProjectTeamReqDTO
     * @return String
     */
    @PostMapping("/{projectId}")
    @Operation(summary = "프로젝트 멤버 추가")
    private BaseResponse<String> postPojectMember(@RequestHeader Long memberId,
                                                  @PathVariable("projectId") Long projectId,
                                                  @RequestBody List<ProjectTeamReqDTO> projectTeamReqDTOList) {
        String postPojectMemberRes = projectService.postProjectMember(memberId, projectId, projectTeamReqDTOList);

        return new BaseResponse<>(postPojectMemberRes);
    }

    /**
     * 프로젝트 멤버 삭제를 위한 API
     * @param memberId 삭제하는 작업을 하는 멤버의 아이디
     * @param belongId 삭제할 멤버의 소속 아이디
     * @return String
     */
    @PatchMapping("/{belongId}")
    @Operation(summary = "프로젝트 멤버 삭제")
    private BaseResponse<String> deleteTeamMember(@RequestHeader Long memberId,
                                                  @PathVariable("belongId") Long belongId) {
        String deleteTeamMemberRes = belongService.deleteTeamMember(memberId, belongId);

        return new BaseResponse<>(deleteTeamMemberRes);
    }

    /**
     * 프로젝트 멤버 권한 변경 API
     * @param memberId 변경 작업을 하는 멤버의 아이디
     * @param projectId 프로젝트 아이디
     * @param belongId 변경할 소속 아이디
     * @param grade 변경할 권한
     * @return String
     */
    @PatchMapping("/grade/{projectId}/{belongId}")
    @Operation(summary = "프로젝트 멤버 권한 변경")
    @ApiResponses({
            @ApiResponse(responseCode = "700", description = "유저에게 해당 권한이 없습니다."),
            @ApiResponse(responseCode = "703", description = "소속 아이디 값을 확인해주세요.")
    })
    private BaseResponse<String> patchGrade(@RequestHeader Long memberId,
                                            @PathVariable("projectId") Long projectId,
                                            @PathVariable("belongId") Long belongId,
                                            @RequestBody GradeType grade) {
        String patchGradeRes = belongService.patchGrade(memberId, projectId, belongId, grade);

        return new BaseResponse<>(patchGradeRes);
    }

    /**
     * 프로젝트 멤버 리스트 조회 API
     * @param memberId 조회 작업을 하는 멤버의 아이디
     * @param projectId 프로젝트 아이디
     * @return GetBelongResDTO
     */
    @GetMapping("/list/{projectId}")
    @Operation(summary = "프로젝트 멤버 리스트 조회")
    private BaseResponse<List<GetBelongResDTO>> getBelongList(@RequestHeader Long memberId,
                                                              @PathVariable("projectId") Long projectId) {
        List<GetBelongResDTO> getBelongResDTOList = belongService.getBelongList(memberId, projectId);

        return new BaseResponse<>(getBelongResDTOList);
    }

}
