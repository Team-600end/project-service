package com.batton.projectservice.mq;

import com.batton.projectservice.mq.dto.NoticeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RabbitProducer {
    private final RabbitTemplate rabbitTemplate;

    public String sendNoticeMessage(NoticeMessage noticeMessage) {
        rabbitTemplate.convertAndSend("notice-exchange", "notice.key", noticeMessage);
        rabbitTemplate.convertAndSend("notice-exchange", "user.key" + noticeMessage.getReceiverId().toString(), noticeMessage);

        return "알림 데이터 전송 완료";
    }
}
