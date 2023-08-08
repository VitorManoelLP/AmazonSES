package com.moneyflow.flow.service;

import com.moneyflow.flow.domain.EmailLog;
import com.moneyflow.flow.dto.EmailStructureDTO;
import com.moneyflow.flow.service.imp.EmailSendResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSendConsumer {

    private final List<EmailSendResolver> resolvers;

    @RabbitListener(queues = EmailLog.DEFAULT_QUEUE)
    public void processEmailEvent(final EmailStructureDTO emailStructure) {
        try {
            EmailSendResolver.extractServiceAndSend(resolvers, emailStructure);
        } catch (Exception ex) {
            log.error("Ocorreu um erro no processamento da fila -> ", ex);
        }
    }

}
