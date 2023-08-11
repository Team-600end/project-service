package com.batton.projectservice.common;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {
    /**
     * Default status code
     */
    SUCCESS(true, 200, "요청에 성공하였습니다."),
    NOT_FOUND(false, 404, "요청을 찾을 수 없습니다."),
    SERVER_ERROR(false, 500, "서버 처리에 오류가 발생하였습니다."),

    /**
     * project-service - 700 ~ 1299
     */

    MEMBER_NO_AUTHORITY(false, 700, "유저에게 해당 권한이 없습니다."),
    PROJECT_INVALID_ID(false, 701, "프로젝트 아이디 값을 확인해주세요."),
    USER_INVALID_ID(false, 702, "유저 아이디 값을 확인해주세요."),
    BELONG_INVALID_ID(false, 703, "소속 아이디 값을 확인해주세요."),
    ISSUE_INVALID_ID(false, 704, "이슈 아이디 값을 확인해주세요."),
    ISSUE_REPORT_INVALID_ID(false, 705, "이슈 레포트 아이디 값을 확인해주세요."),
    ISSUE_REPORT_EXISTS(false, 706, "이슈 레포트가 이미 존재합니다."),
    PROJECT_NOT_EXISTS(false, 707, "참여한 프로젝트가 없습니다."),
    RELEASE_NOTE_INVALID_ID(false, 708, "릴리즈 노트 아이디 값을 확인해주세요."),
    PROJECT_KEY_EXISTS(false, 708, "중복된 프로젝트 키입니다."),
    IMAGE_UPLOAD_ERROR(false, 710, "이미지 업로드에 실패하였습니다.");


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
