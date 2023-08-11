package com.batton.projectservice.controller;

import com.batton.projectservice.common.BaseResponse;
import com.batton.projectservice.dto.comment.PostCommentReqDTO;
import com.batton.projectservice.dto.report.GetAddReportResDTO;
import com.batton.projectservice.dto.report.GetIssueReportResDTO;
import com.batton.projectservice.dto.report.PatchIssueReportReqDTO;
import com.batton.projectservice.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reports")
public class ReportController {
    private final ReportService reportService;

    /**
     * 이슈 레포트 조회 API
     * @param memberId 조회 작업을 하는 멤버의 아이디
     * @param issueId 조회할 이슈 레포트의 이슈 아이디
     * @return
     * */
    @GetMapping("/{issueId}")
    @Operation(summary = "이슈 레포트 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "703", description = "소속 아이디 값을 확인해주세요."),
            @ApiResponse(responseCode = "704", description = "이슈 아이디 값을 확인해주세요."),
    })
    private BaseResponse<GetIssueReportResDTO> getReport(@RequestHeader Long memberId, @PathVariable("issueId") Long issueId) {
        GetIssueReportResDTO getIssueReportResDTO = reportService.getReport(memberId, issueId);

        return new BaseResponse<>(getIssueReportResDTO);
    }

    /**
     * 이슈 레포트 수정 API
     * @param memberId 수정 작업을 하는 멤버의 아이디
     * @param issueId 수정할 이슈 레포트의 이슈 아이디
     * @param patchIssueReportReqDTO 이슈 레포트 수정 요청 요청 바디에 포함될 PatchIssueReportReqDTO
     * @return String
     * */
    @PatchMapping("/{issueId}")
    @Operation(summary = "이슈 레포트 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "703", description = "소속 아이디 값을 확인해주세요."),
            @ApiResponse(responseCode = "704", description = "이슈 아이디 값을 확인해주세요."),
            @ApiResponse(responseCode = "705", description = "이슈 레포트 아이디 값을 확인해주세요.")
    })
    private BaseResponse<String> patchReport(@RequestHeader Long memberId, @PathVariable("issueId") Long issueId, @RequestBody PatchIssueReportReqDTO patchIssueReportReqDTO) {
        String patchReportRes = reportService.patchReport(memberId, issueId, patchIssueReportReqDTO);

        return new BaseResponse<>(patchReportRes);
    }

    /**
     * 이슈 코멘트 생성 API
     * @param memberId 코멘트 생성하는 유저 아이디
     * @param issueId 코멘트를 작성할 이슈 아이디
     * @param postCommentReqDTO 생성 요청 바디에 포함될 PostCommentReqDTO
     * @return String
     * */
    @PostMapping("/comments/{issueId}")
    @Operation(summary = "이슈 코멘트 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "700", description = "유저에게 해당 권한이 없습니다."),
            @ApiResponse(responseCode = "703", description = "소속 아이디 값을 확인해주세요."),
            @ApiResponse(responseCode = "704", description = "이슈 아이디 값을 확인해주세요.")
    })
    private BaseResponse<String> postComment(@RequestHeader Long memberId, @PathVariable("issueId") Long issueId, @RequestBody PostCommentReqDTO postCommentReqDTO) {
        String postCommentRes = reportService.postComment(issueId, memberId, postCommentReqDTO);

        return new BaseResponse<>(postCommentRes);
    }

    /**
     * 추가할 이슈 레포트 조회 API
     * @param issueId 조회할 이슈 아이디
     */
    @GetMapping("/releases/{issueId}")
    @Operation(summary = "추가할 이슈 레포트 조회")
    @ApiResponse(responseCode = "705", description = "이슈 레포트 아이디 값을 확인해주세요.")
    private BaseResponse<GetAddReportResDTO> getAddReport(@PathVariable("issueId") Long issueId) {
        GetAddReportResDTO getAddReportResDTO = reportService.getAddReport(issueId);

        return new BaseResponse<>(getAddReportResDTO);
    }
}
