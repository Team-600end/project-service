package com.batton.projectservice.controller;

import com.batton.projectservice.common.BaseResponse;
import com.batton.projectservice.dto.issue.*;
import com.batton.projectservice.dto.issue.GetIssueReportResDTO;
import com.batton.projectservice.enums.IssueStatus;
import com.batton.projectservice.service.IssueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/issues")
public class IssueController {
    private final IssueService issueService;

    /**
     * 이슈 생성 API
     * @param memberId 이슈 생성하는 유저 아이디
     * @param postIssueReqDTO 생성 요청 바디에 포함될 PostIssueReqDTO
     * @return IssueId
     */
    @PostMapping
    @Operation(summary = "이슈 생성 요청")
    @ApiResponses({
            @ApiResponse(responseCode = "701", description = "프로젝트 아이디 값을 확인해주세요."),
            @ApiResponse(responseCode = "703", description = "소속 아이디 값을 확인해주세요.")
    })
    public BaseResponse<Long> postIssue(@RequestHeader Long memberId, @RequestBody PostIssueReqDTO postIssueReqDTO) {
        Long postIssueRes = issueService.postIssue(memberId, postIssueReqDTO);

        return new BaseResponse<>(postIssueRes);
    }

    /**
     * 이슈 보드 상태 및 순서 변경 API
     * @param memberId 변경하는 작업을 하는 유저 아이디
     * @param issueId 상태를 변경할 이슈 아이디
     * @param patchIssueBoardReqDTO 변경 요청 바디에 포함될 PatchIssueBoardReqDTO
     * @return String
     * */
    @PatchMapping("/board/status/{issueId}")
    @Operation(summary = "이슈 보드 상태 및 순서 변경")
    @ApiResponses({
            @ApiResponse(responseCode = "700", description = "유저에게 해당 권한이 없습니다."),
            @ApiResponse(responseCode = "703", description = "소속 아이디 값을 확인해주세요."),
            @ApiResponse(responseCode = "704", description = "이슈 아이디 값을 확인해주세요.")
    })
    private BaseResponse<String> patchIssueBoard(@RequestHeader Long memberId, @PathVariable("issueId") Long issueId, @RequestBody PatchIssueBoardReqDTO patchIssueBoardReqDTO) {
        String patchIssueBoardRes = issueService.patchIssueBoard(memberId, issueId, patchIssueBoardReqDTO);

        return new BaseResponse<>(patchIssueBoardRes);
    }

    /**
     * 이슈 보드 목록 조회
     * @param memberId 조회하는 유저 아이디
     * @param projectId 이슈 조회할 프로젝트 아이디
     * @return GetIssueBoardResDTO
     * */
    @GetMapping("/board/list/{projectId}")
    @Operation(summary = "이슈 보드 목록 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "701", description = "프로젝트 아이디 값을 확인해주세요."),
            @ApiResponse(responseCode = "703", description = "소속 아이디 값을 확인해주세요.")
    })
    private BaseResponse<GetIssueBoardResDTO> getIssueBoard(@RequestHeader Long memberId, @PathVariable("projectId") Long projectId) {
        GetIssueBoardResDTO getIssueBoardResDTO = issueService.getIssueBoard(memberId, projectId);

        return new BaseResponse<>(getIssueBoardResDTO);
    }

    /**
     * 이슈 도넛차트 조회
     * @param memberId 조회하는 유저 아이디
     * @param projectId 이슈 조회할 프로젝트 아이디
     * @return GetIssueChartResDTO
     * */
    @GetMapping("/chart/{projectId}")
    @Operation(summary = "이슈 도넛차트 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "701", description = "프로젝트 아이디 값을 확인해주세요."),
            @ApiResponse(responseCode = "703", description = "소속 아이디 값을 확인해주세요.")
    })
    private BaseResponse<GetIssueChartResDTO> getIssueChart(@RequestHeader Long memberId, @PathVariable("projectId") Long projectId) {
        GetIssueChartResDTO getIssueChartResDTO = issueService.getIssueChart(memberId, projectId);

        return new BaseResponse<>(getIssueChartResDTO);
    }

    /**
     * 개인 이슈 목록 조회 API
     * @param memberId 조회하는 유저 아이디
     * @param issueStatus 조회할 이슈 상태
     * @param keyword 조회할 이슈 제목
     * @return List<GetMyIssueResDTO>
     *
     * */
    @GetMapping("/list")
    @Operation(summary = "개인 이슈 목록 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "703", description = "소속 아이디 값을 확인해주세요."),
            @ApiResponse(responseCode = "704", description = "이슈 아이디 값을 확인해주세요.")
    })
    private BaseResponse<List<GetMyIssueResDTO>> getMyIssue(@RequestHeader Long memberId, @RequestParam(value = "status", required = false) IssueStatus issueStatus, @RequestParam(value = "keyword", required = false) String keyword) {
        List<GetMyIssueResDTO> getMyIssueResDTOList = issueService.getMyIssue(memberId, issueStatus, keyword);

        return new BaseResponse<>(getMyIssueResDTOList);
    }

    /**
     * 이슈 관리 페이지 조회 API
     * @param memberId 조회하는 유저 아이디
     * @param issueId 조회할 이슈 아이디
     * @return GetIssueInfoResDTO
     * */
    @GetMapping("/{issueId}")
    @Operation(summary = "이슈 관리 페이지 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "703", description = "소속 아이디 값을 확인해주세요."),
            @ApiResponse(responseCode = "704", description = "이슈 아이디 값을 확인해주세요.")
    })
    private BaseResponse<GetIssueInfoResDTO> getIssueInfo(@RequestHeader Long memberId, @PathVariable("issueId") Long issueId) {
        GetIssueInfoResDTO getIssueInfoResDTO = issueService.getIssueInfo(memberId, issueId);

        return new BaseResponse<>(getIssueInfoResDTO);
    }

    /**
     * 이슈 조회 페이지 조회 API
     * @param issueId 조회할 이슈 아이디
     */
    @GetMapping("/reports/{issueId}")
    @Operation(summary = "이슈 조회 페이지 조회")
    @ApiResponse(responseCode = "704", description = "이슈 아이디 값을 확인해주세요.")
    private BaseResponse<GetIssueReportResDTO> getIssueReport(@PathVariable("issueId") Long issueId) {
        GetIssueReportResDTO getIssueReportResDTO = issueService.getIssueReport(issueId);

        return new BaseResponse<>(getIssueReportResDTO);
    }

    /**
     * 대시보드 이슈 리스트 조회 API
     * @param memberId 조회하는 유저 아이디
     * @param projectId 조회할 프로젝트의 아이디
     * @return List<GetIssueResDTO>
     * */
    @GetMapping("/projects/list/{projectId}")
    @Operation(summary = "대시보드 이슈 리스트 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "703", description = "소속 아이디 값을 확인해주세요."),
            @ApiResponse(responseCode = "704", description = "이슈 아이디 값을 확인해주세요.")
    })
    private BaseResponse<List<GetIssueResDTO>> getIssueList(@RequestHeader Long memberId, @PathVariable("projectId") Long projectId) {
        List<GetIssueResDTO> getIssueResDTOList = issueService.getIssueList(memberId, projectId);

        return new BaseResponse<>(getIssueResDTOList);
    }

    /**
     * 이슈 히스토리 목록 조회 API
     * @param memberId 조회하는 유저 아이디
     * @param projectId 조회할 프로젝트의 아이디
     * @return List<GetIssueResDTO>
     * */
    @GetMapping("/history/list/{projectId}")
    @Operation(summary = "이슈 히스토리 목록 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "703", description = "소속 아이디 값을 확인해주세요."),
            @ApiResponse(responseCode = "704", description = "이슈 아이디 값을 확인해주세요.")
    })
    private BaseResponse<List<GetIssueResDTO>> getIssueHistory(@RequestHeader Long memberId, @PathVariable("projectId") Long projectId) {
        List<GetIssueResDTO> getIssueResDTOList = issueService.getIssueHistory(memberId, projectId);

        return new BaseResponse<>(getIssueResDTOList);
    }

    /**
     * 이슈 수정 API
     * @param memberId 이슈 수정하는 유저 아이디
     * @param issueId 수정할 이슈 아이디
     * @param patchIssueReqDTO 수정 요청 바디에 포함될 PatchIssueReqDTO
     * @return String
     */
    @PatchMapping("/{issueId}")
    @Operation(summary = "이슈 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "700", description = "유저에게 해당 권한이 없습니다."),
            @ApiResponse(responseCode = "703", description = "소속 아이디 값을 확인해주세요."),
            @ApiResponse(responseCode = "704", description = "이슈 아이디 값을 확인해주세요.")
    })
    private BaseResponse<String> patchIssue(@RequestHeader Long memberId, @PathVariable("issueId") Long issueId, @RequestBody PatchIssueReqDTO patchIssueReqDTO) {
        String patchIssueRes = issueService.patchIssue(memberId, issueId, patchIssueReqDTO);

        return new BaseResponse<>(patchIssueRes);
    }

    /**
     * 이슈 수정 정보 조회 API
     * @param memberId 이슈 수정하는 유저 아이디
     * @param issueId 수정할 이슈 아이디
     * @return String
     */
    @GetMapping("/{issueId}/fetch")
    @Operation(summary = "이슈 수정 정보 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "700", description = "유저에게 해당 권한이 없습니다."),
            @ApiResponse(responseCode = "703", description = "소속 아이디 값을 확인해주세요."),
            @ApiResponse(responseCode = "704", description = "이슈 아이디 값을 확인해주세요.")
    })
    private BaseResponse<GetModifyIssueResDTO> getModifyIssue(@RequestHeader Long memberId, @PathVariable("issueId") Long issueId) {
        GetModifyIssueResDTO getModifyIssueResDTO = issueService.getModifyIssue(memberId, issueId);

        return new BaseResponse<>(getModifyIssueResDTO);
    }

    /**
     * 이슈 삭제 API
     * @param memberId 이슈 삭제하는 유저 아이디
     * @param issueId 삭제할 이슈 아이디
     * @return String
     */
    @DeleteMapping("/{issueId}")
    @Operation(summary = "이슈 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "703", description = "소속 아이디 값을 확인해주세요."),
            @ApiResponse(responseCode = "704", description = "이슈 아이디 값을 확인해주세요.")
    })
    private BaseResponse<String> deleteIssue(@RequestHeader Long memberId, @PathVariable("issueId") Long issueId) {
        String deleteIssueRes = issueService.deleteIssue(memberId, issueId);

        return new BaseResponse<>(deleteIssueRes);
    }

    /**
     * 완료된 이슈 목록 조회 API
     * @param projectId 조회할 프로젝트의 아이디
     * @return List<GetIssueResDTO>
     */
    @GetMapping("/{projectId}/done-list")
    @Operation(summary = "완료된 이슈 목록 조회")
    private BaseResponse<List<GetIssueResDTO>> getDoneIssue(@PathVariable("projectId") Long projectId) {
        List<GetIssueResDTO> getIssueResDTOList = issueService.getDoneIssue(projectId);

        return new BaseResponse<>(getIssueResDTOList);
    }

    /**
     * 이슈 바톤 터치 API
     * @param memberId 바톤 터치를 수행하는 유저
     * @param issueId 바톤 터치 이슈
     * @return String
     */
    @PostMapping("/{issueId}")
    @Operation(summary = "이슈 바톤 터치")
    private BaseResponse<String> postBattonTouch(@RequestHeader Long memberId,
                                                 @PathVariable("issueId") Long issueId,
                                                 @RequestBody PostBattonTouchReqDTO postBattonTouchReqDTO) {
        String result = issueService.postBattonTouch(issueId, postBattonTouchReqDTO);

        return new BaseResponse<>(result);
    }
}
