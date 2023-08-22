package com.moneyflow.flow.service;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.moneyflow.flow.dto.EmailStructureDTO;
import com.moneyflow.flow.enums.TypeMail;
import com.moneyflow.flow.service.imp.EmailSendResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceBasic implements EmailSendResolver {

    private final AmazonSimpleEmailService amazonSimpleEmailService;

    @Override
    public TypeMail getTypeMail() {
        return TypeMail.BASIC;
    }

    @Override
    public void send(final EmailStructureDTO emailStructure) {

        log.info("Iniciando envio de e-mail pelo AWS SES com a estrutura -> {}", emailStructure);

        EmailStructureDTO.AwsFormat awsFormat = emailStructure.toAwsSend();

        final SendEmailRequest sendEmailRequest = new SendEmailRequest()
                .withSource(awsFormat.getSender())
                .withDestination(awsFormat.getDestination())
                .withMessage(awsFormat.getMessage());

        amazonSimpleEmailService.sendEmail(sendEmailRequest);
    }
}
