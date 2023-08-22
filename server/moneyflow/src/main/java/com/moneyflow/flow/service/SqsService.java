package com.moneyflow.flow.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moneyflow.flow.dto.EmailStructureDTO;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SqsService {

    private final QueueMessagingTemplate queueMessagingTemplate;
    private final ObjectMapper objectMapper;

    public void sendMessage(final EmailStructureDTO emailStructure) {
        queueMessagingTemplate.send("email-log-queue", MessageBuilder.withPayload(getPayload(emailStructure)).build());
    }

    @SneakyThrows
    private String getPayload(EmailStructureDTO emailStructure) {
        return objectMapper.writeValueAsString(emailStructure);
    }

}
