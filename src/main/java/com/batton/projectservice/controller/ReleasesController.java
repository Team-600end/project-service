package com.batton.projectservice.controller;

import com.batton.projectservice.common.BaseResponse;
import com.batton.projectservice.dto.release.*;
import com.batton.projectservice.enums.IssueStatus;
import com.batton.projectservice.service.ReleasesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/releases")
public class ReleasesController {
    private final ReleasesService releasesService;

    /**
     * 릴리즈 노트 생성 API
     * @param memberId 릴리즈 노트 생성하는 유저 아이디
     * @param postReleasesReqDTO 릴리즈 노트 생성 요청 바디에 포함될 PostReleasesReqDTO
     * @return releasesId
     */
    @PostMapping
    @Operation(summary = "릴리즈노트 생성 요청")
    private BaseResponse<Long> postReleases(@RequestHeader Long memberId,
                                            @RequestBody PostReleasesReqDTO postReleasesReqDTO) {
        Long postReleasesRes = releasesService.postReleases(memberId, postReleasesReqDTO);

        return new BaseResponse<>(postReleasesRes);
    }

    /**
     * 릴리즈 노트 발행 API
     * @param memberId 릴리즈 노트 발행하는 유저 아이디
     * @param releaseId 발행할 릴리즈 노트 아이디
     */
    @PatchMapping("/{releaseId}/status")
    @Operation(summary = "릴리즈노트 발행 요청")
    @ApiResponses({
            @ApiResponse(responseCode = "700", description = "유저에게 해당 권한이 없습니다."),
            @ApiResponse(responseCode = "703", description = "소속 아이디 값을 확인해주세요."),
            @ApiResponse(responseCode = "708", description = "릴리즈 노트 아이디 값을 확인해주세요.")
    })
    private BaseResponse<String> patchPublish(@RequestHeader Long memberId,
                                              @PathVariable("releaseId") Long releaseId) {
        String publishReleasesRes = releasesService.patchPublish(memberId, releaseId);

        return new BaseResponse<>(publishReleasesRes);
    }

    /**
     * 릴리즈 노트 수정 API
     * @param memberId 릴리즈 노트 수정하는 유저 아이디
     * @param releaseId 발행할 릴리즈 노트 아이디
     * @param patchReleasesReqDTO 릴리즈 노트 수정 요청 바디에 포함될 PatchReleasesReqDTO
     * @return String
     */
    @PatchMapping("/{releaseId}")
    @Operation(summary = "릴리즈노트 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "700", description = "유저에게 해당 권한이 없습니다."),
            @ApiResponse(responseCode = "703", description = "소속 아이디 값을 확인해주세요."),
            @ApiResponse(responseCode = "708", description = "릴리즈 노트 아이디 값을 확인해주세요.")
    })
    private BaseResponse<String> patchReleases(@RequestHeader Long memberId,
                                               @PathVariable Long releaseId,
                                               @RequestBody PatchReleasesReqDTO patchReleasesReqDTO) {
        String patchReleasesRes = releasesService.patchReleases(memberId, releaseId, patchReleasesReqDTO);

        return new BaseResponse<>(patchReleasesRes);
    }

    /**
     * 릴리즈 노트 삭제 API
     * @param memberId 릴리즈 노트 삭제하는 유저 아이디
     * @param releaseId 삭제할 릴리즈 노트 아이디
     */
    @DeleteMapping("/{releaseId}")
    @Operation(summary = "릴리즈노트 삭제 요청")
    @ApiResponses({
            @ApiResponse(responseCode = "700", description = "유저에게 해당 권한이 없습니다."),
            @ApiResponse(responseCode = "703", description = "소속 아이디 값을 확인해주세요."),
            @ApiResponse(responseCode = "708", description = "릴리즈 노트 아이디 값을 확인해주세요.")
    })
    private BaseResponse<String> deleteReleases(@RequestHeader Long memberId,
                                                @PathVariable("releaseId") Long releaseId) {
        String deleteReleasesRes = releasesService.deleteReleases(memberId, releaseId);

        return new BaseResponse<>(deleteReleasesRes);
    }

    /**
     * 릴리즈 노트에 포함된 이슈 목록 조회 API
     * @param releaseId 조회할 릴리즈 노트 아이디
     */
    @GetMapping("/{releaseId}/issues/list")
    @Operation(summary = "릴리즈노트에 포함된 이슈 목록 조회 요청")
    @ApiResponses({
            @ApiResponse(responseCode = "704", description = "이슈 아이디 값을 확인해주세요."),
            @ApiResponse(responseCode = "708", description = "릴리즈 노트 아이디 값을 확인해주세요.")
    })
    private BaseResponse<List<GetReleasesIssueResDTO>> getReleasesIssues(@PathVariable("releaseId") Long releaseId) {
        List<GetReleasesIssueResDTO> getReleasesIssueRes = releasesService.getReleasesIssues(releaseId);

        return new BaseResponse<>(getReleasesIssueRes);
    }

    /**
     * 릴리즈 노트 상세 조회 API
     * @param releaseId 조회할 릴리즈 노트 아이디
     */
    @GetMapping("/{releaseId}")
    @Operation(summary = "릴리즈노트 상세 조회 요청")
    @ApiResponses({
            @ApiResponse(responseCode = "708", description = "릴리즈 노트 아이디 값을 확인해주세요.")
    })
    private BaseResponse<GetReleasesResDTO> getReleases(@RequestHeader Long memberId,
                                                        @PathVariable("releaseId") Long releaseId) {
        GetReleasesResDTO getReleasesRes = releasesService.getReleases(memberId, releaseId);

        return new BaseResponse<>(getReleasesRes);
    }

    /**
     * 릴리즈 노트 수정용 상세 조회 API
     * @param memberId 릴리즈 노트 수정하는 유저 아이디
     * @param releaseId 조회할 릴리즈 노트 아이디
     */
    @GetMapping("/editor/{releaseId}")
    @Operation(summary = "릴리즈노트 수정용 상세 조회 요청")
    @ApiResponses({
            @ApiResponse(responseCode = "708", description = "릴리즈 노트 아이디 값을 확인해주세요.")
    })
    private BaseResponse<GetReleasesEditResDTO> getReleasesEdit(@RequestHeader Long memberId,
                                                                @PathVariable("releaseId") Long releaseId) {
        GetReleasesEditResDTO getReleasesEditRes = releasesService.getReleasesEdit(memberId, releaseId);

        return new BaseResponse<>(getReleasesEditRes);
    }

    /**
     * 프로젝트 릴리즈 노트 조회 API
     * @param projectId 조회할 프로젝트 아이디
     * @return List<GetProjectReleaseListResDTO>
     */
    @GetMapping("/project/{projectId}")
    @Operation(summary = "프로젝트 릴리즈 노트 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "703", description = "소속 아이디 값을 확인해주세요."),
            @ApiResponse(responseCode = "709", description = "프로젝트 아이디 값을 확인해주세요.")
    })
    public BaseResponse<GetReleasesAllResDTO> getProjectReleaseList(@RequestHeader Long memberId,
                                                                    @PathVariable Long projectId) {
        GetReleasesAllResDTO getReleasesAllResDTO = releasesService.getProjectReleasesList(memberId, projectId);

        return new BaseResponse<>(getReleasesAllResDTO);
    }

    /**
     * 게시판 조회 API
     * @param projectId 조회할 프로젝트 아이디
     * @param keyword 조회할 내용
     * @return List<GetMyIssueResDTO>
     * */
    @GetMapping
    @Operation(summary = "게시판 조회")
    private BaseResponse<List<GetReleasesBoardResDTO>> getReleases(@RequestParam(value = "projectId", required = false) Long projectId,
                                                                   @RequestParam(value = "keyword", required = false) String keyword) {
        List<GetReleasesBoardResDTO> getReleasesBoardResDTOList = releasesService.getRelease(projectId, keyword);

        return new BaseResponse<>(getReleasesBoardResDTOList);
    }

    /**
     * 이미지 업로드 API
     * @param memberId 정보를 수정할 유저 아이디
     * @param profileImage 변경할 이미지
     * @return String
     * */
    @PostMapping("/images/upload")
    @Operation(summary = "이미지 업로드")
    private BaseResponse<String> postImage(@RequestHeader Long memberId,
                                           @RequestPart(value = "profileImg", required = false) MultipartFile profileImage) {
        String url = releasesService.postImage(memberId, profileImage);

        return new BaseResponse<>(url);
    }
}
