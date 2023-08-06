package com.moneyflow.flow.service;

import com.moneyflow.flow.domain.EmailLog;
import com.moneyflow.flow.dto.EmailStructureDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSendProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendEvent(final EmailStructureDTO emailStructure) {
        rabbitTemplate.convertAndSend(EmailLog.DEFAULT_QUEUE, emailStructure);
    }

}
