package com.moneyflow.flow.service;

import com.moneyflow.flow.domain.EmailLog;
import com.moneyflow.flow.dto.EmailStructureDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmailSendConsumer {

    private final EmailSendService emailSendService;

    @RabbitListener(queues = EmailLog.DEFAULT_QUEUE)
    public void processEmailEvent(final EmailStructureDTO emailStructure) {
        emailSendService.send(emailStructure);
    }

}
