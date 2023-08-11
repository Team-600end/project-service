package com.batton.projectservice.enums;

/**
 * INVITE - 프로젝트 초대 알림 (1)
 * EXCLUDE - 프로젝트 제외(방출) 알림 (1)
 * NEW - 새 릴리즈 노트 출시 알림 (n)
 * REVIEW - 검토 이슈 발생 알림 (n)
 * APP ROVE - 이슈 승인 알림 (1)
 * REJECT - 이슈 반려 알림 (1)
 * BATTON - 이슈 바톤 터치 알림 (1)
 * COMMENT - 댓글 알림 (1)
 * GRADE - 권한 변경 (1)
 */
public enum NoticeType {
    INVITE, EXCLUDE, REVIEW, APPROVE, REJECT, BATTON, NEW, COMMENT, GRADE
}
