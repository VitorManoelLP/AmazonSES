package com.moneyflow.flow.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.moneyflow.flow.FlowApplication;
import com.moneyflow.flow.dto.EmailStructureDTO;
import com.moneyflow.flow.service.imp.EmailSendResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailServiceLambda implements RequestHandler<EmailStructureDTO, Boolean> {

    private final List<EmailSendResolver> resolvers;

    @Override
    public Boolean handleRequest(EmailStructureDTO input, Context context) {

        try {
            EmailSendResolver.extractServiceAndSend(resolvers, input);
            return true;
        } catch (AmazonClientException ex) {
            log.error("Ocorreu um problema no envio do e-mail", ex);
            return false;
        }

    }

}
