package com.moneyflow.flow.service;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.moneyflow.flow.domain.EmailLog;
import com.moneyflow.flow.dto.EmailStructureDTO;
import com.moneyflow.flow.enums.TypeMail;
import com.moneyflow.flow.repository.EmailLogRepository;
import com.moneyflow.flow.service.imp.EmailSendResolver;
import com.moneyflow.flow.util.AwsUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceBasic implements EmailSendResolver {

    private final AmazonSimpleEmailService amazonSimpleEmailService;
    private final EmailLogRepository emailLogRepository;
    private final RunNewTransactionService runNewTransactionService;

    @Override
    public TypeMail getTypeMail() {
        return TypeMail.BASIC;
    }

    @Override
    @Transactional
    public void send(final EmailStructureDTO emailStructure) {

        log.info("Iniciando envio de e-mail pelo AWS SES com a estrutura -> {}", emailStructure);

        EmailStructureDTO.AwsFormat awsFormat = emailStructure.toAwsSend();
        final EmailLog entity = emailStructure.toEntity();

        final SendEmailRequest sendEmailRequest = new SendEmailRequest()
                .withSource(awsFormat.getSender())
                .withDestination(awsFormat.getDestination())
                .withMessage(awsFormat.getMessage());

        sendEmail(entity, sendEmailRequest);

        runNewTransactionService.run(() -> emailLogRepository.save(entity));
    }

    private void sendEmail(final EmailLog entity, SendEmailRequest aws) {
        AwsUtil.tryRun(() -> amazonSimpleEmailService.sendEmail(aws), (exception) -> {
            log.error("Tentativa de envio de e-mail falhou", exception);
            entity.setError(exception.getMessage());
        });
    }
}
