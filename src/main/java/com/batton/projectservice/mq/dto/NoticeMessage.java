package com.batton.projectservice.mq.dto;

import com.batton.projectservice.enums.NoticeType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NoticeMessage {
    private Long projectId;
    private Long senderId;
    private Long receiverId;
    private Long contentId;
    private NoticeType noticeType;
    private String noticeContent;

    @Builder
    public NoticeMessage(Long projectId, Long senderId, Long receiverId, Long contentId, NoticeType noticeType, String noticeContent) {
        this.projectId = projectId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.contentId = contentId;
        this.noticeType = noticeType;
        this.noticeContent = noticeContent;
    }
}
