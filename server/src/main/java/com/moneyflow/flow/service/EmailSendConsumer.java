package com.moneyflow.flow.service;

import com.moneyflow.flow.domain.EmailLog;
import com.moneyflow.flow.dto.EmailStructureDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSendConsumer {

    private final EmailService emailSendService;

    @RabbitListener(queues = EmailLog.DEFAULT_QUEUE)
    public void processEmailEvent(final EmailStructureDTO emailStructure) {
        try {
            emailSendService.sendVerificationMail(emailStructure);
        } catch (Exception ex) {
            log.error("Ocorreu um erro no processamento da fila -> ", ex);
        }
    }

}
