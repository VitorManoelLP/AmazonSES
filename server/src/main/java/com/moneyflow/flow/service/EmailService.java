package com.moneyflow.flow.service;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.*;
import com.moneyflow.flow.domain.EmailLog;
import com.moneyflow.flow.dto.EmailStructureDTO;
import com.moneyflow.flow.repository.EmailLogRepository;
import com.moneyflow.flow.util.AwsUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final AmazonSimpleEmailService amazonSimpleEmailService;
    private final EmailLogRepository emailLogRepository;
    private final RunNewTransactionService runNewTransactionService;

    @Transactional
    public void sendVerificationMail(final EmailStructureDTO emailStructure) {

        log.info("Iniciando envio de e-mail pelo AWS SES com a estrutura -> {}", emailStructure);

        final VerifyEmailAddressRequest aws = emailStructure.toAws();
        final EmailLog entity = emailStructure.toEntity();

        sendEmail(entity, aws);

        runNewTransactionService.run(() -> emailLogRepository.save(entity));
    }

    public List<String> getAllVerifiedMails() {
        return amazonSimpleEmailService.listVerifiedEmailAddresses().getVerifiedEmailAddresses();
    }

    private void sendEmail(final EmailLog entity, VerifyEmailAddressRequest aws) {
        AwsUtil.tryRun(() -> amazonSimpleEmailService.verifyEmailAddress(aws), (exception) -> {
            log.error("Tentativa de envio de e-mail falhou", exception);
            entity.setError(exception.getMessage());
        });
    }

}
