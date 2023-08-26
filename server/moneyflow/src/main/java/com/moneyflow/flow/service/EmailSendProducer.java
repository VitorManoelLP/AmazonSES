package com.moneyflow.flow.service;

import com.moneyflow.flow.domain.EmailLog;
import com.moneyflow.flow.dto.EmailStructureDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSendProducer {

    private final SqsService sqsService;

    public void sendEvent(final EmailStructureDTO emailStructure) {
        sqsService.sendMessage(emailStructure, EmailLog.DEFAULT_QUEUE);
    }

}
