package com.moneyflow.flow.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.*;
import com.moneyflow.flow.domain.EmailLog;
import com.moneyflow.flow.dto.EmailStructureDTO;
import com.moneyflow.flow.repository.EmailLogRepository;
import com.moneyflow.flow.util.AwsUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailSendService {

    private final AmazonSimpleEmailService amazonSimpleEmailService;
    private final EmailLogRepository emailLogRepository;
    private final RunNewTransactionService runNewTransactionService;

    @Transactional
    public void sendVerificationMail(final EmailStructureDTO emailStructure) {

        log.info("Iniciando envio de e-mail pelo AWS SES com a estrutura -> {}", emailStructure);

        final SendCustomVerificationEmailRequest aws = emailStructure.toAws();
        final EmailLog entity = emailStructure.toEntity();

        sendEmail(entity, aws);

        runNewTransactionService.run(() -> emailLogRepository.save(entity));
    }

    private void sendEmail(final EmailLog entity, final SendCustomVerificationEmailRequest aws) {
        AwsUtil.tryRun(() -> amazonSimpleEmailService.sendCustomVerificationEmail(aws), (exception) -> {
            log.error("Tentativa de envio de e-mail falhou", exception);
            entity.setError(exception.getMessage());
        });
    }

}
