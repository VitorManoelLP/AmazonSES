package com.moneyflow.flow.service;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClient;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.QueueDoesNotExistException;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moneyflow.flow.dto.EmailStructureDTO;
import com.moneyflow.flow.service.imp.EmailSendResolver;
import io.awspring.cloud.messaging.listener.SqsMessageDeletionPolicy;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSendConsumer {

    private final List<EmailSendResolver> resolvers;
    private final AmazonSQSAsync amazonSQSAsync;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedDelay = 5000)
    public void processEmailEvent() {

        try {

            final ReceiveMessageResult receiveMessageResult = amazonSQSAsync
                    .receiveMessage("https://sqs.us-east-1.amazonaws.com/319670017522/email-log-queue");

            final List<Message> messages = receiveMessageResult.getMessages();

            if (!messages.isEmpty()) {

                messages.forEach(message -> {

                    try {
                        final String body = message.getBody();
                        EmailSendResolver.extractServiceAndSend(resolvers, objectMapper.readValue(body, EmailStructureDTO.class));
                    } catch (Exception ex) {
                        log.error("Ocorreu um erro no processamento da fila -> ", ex);
                    }

                });

            }

        } catch (QueueDoesNotExistException e) {
            log.error("Queue does not exists", e);
        }

    }

}
