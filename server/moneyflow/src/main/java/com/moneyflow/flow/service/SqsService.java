package com.moneyflow.flow.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moneyflow.flow.domain.EmailLog;
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

    public void sendMessage(final Object payload, final String queue) {
        queueMessagingTemplate.send(queue, MessageBuilder.withPayload(getPayload(payload)).build());
    }

    @SneakyThrows
    private String getPayload(final Object payload) {
        return objectMapper.writeValueAsString(payload);
    }

}
