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
    public void send(final EmailStructureDTO emailStructure) {

        log.info("Iniciando envio de e-mail pelo AWS SES com a estrutura -> {}", emailStructure);

        final EmailStructureDTO.AwsFormat awsFormat = emailStructure.toAws();
        final EmailLog entity = emailStructure.toEntity();

        final SendEmailRequest sendEmailRequest = new SendEmailRequest()
                .withSource(awsFormat.getSender())
                .withDestination(awsFormat.getDestination())
                .withMessage(awsFormat.getMessage());

        amazonSimpleEmailService.verifyEmailAddress(new VerifyEmailAddressRequest().withEmailAddress(String.join("", awsFormat.getDestination().getToAddresses())));

        verifyEmail(sendEmailRequest, entity);
        sendEmail(sendEmailRequest, entity);

        runNewTransactionService.run(() -> emailLogRepository.save(entity));
    }

    private void verifyEmail(SendEmailRequest sendEmailRequest, EmailLog entity) {

        var verifyEmailAddressRequest = new VerifyEmailAddressRequest()
                .withEmailAddress(String.join("", sendEmailRequest.getDestination().getToAddresses()));

        AwsUtil.tryRun(() -> amazonSimpleEmailService.verifyEmailAddress(verifyEmailAddressRequest), (exception) -> {
            log.error("Tentativa de verificação de e-mail falhou", exception);
            entity.setError(exception.getMessage());
        });

    }

    private void sendEmail(SendEmailRequest sendEmailRequest, EmailLog entity) {
        AwsUtil.tryRun(() -> amazonSimpleEmailService.sendEmail(sendEmailRequest), (exception) -> {
            log.error("Tentativa de envio de e-mail falhou", exception);
            entity.setError(exception.getMessage());
        });
    }

}
