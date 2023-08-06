package com.moneyflow.flow.dto;

import com.amazonaws.services.simpleemail.model.*;
import com.moneyflow.flow.domain.EmailLog;
import com.moneyflow.flow.enums.EmailStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter(AccessLevel.PRIVATE)
public class EmailStructureDTO implements Serializable {

    private static final String DEFAULT_SENDER = "vitor.m.lima.p@gmail.com";
    private static final String DEFAULT_SUBJECT = "Confirmação de e-mail";
    private static final String EMAIL_CONFIRMATION_BODY = "Olá,\n\n"
            + "Obrigado por se cadastrar no MoneyFlow! Para continuar utilizando nossa plataforma, por favor, clique no link abaixo para confirmar o seu e-mail:\n\n"
            + "CONFIRMAR E-MAIL: %s\n\n"
            + "Se você não se cadastrou no MoneyFlow, ignore este e-mail.\n\n"
            + "Atenciosamente,\n"
            + "Equipe MoneyFlow";


    @NotNull
    private String body;

    @NotNull
    private String subject;

    @NotNull
    private String recipient;

    @NotNull
    private String sender;

    @Builder.Default
    private final LocalDateTime sendDate = LocalDateTime.now();

    public static EmailStructureDTO newInstanceBySystem(@NotNull @NotEmpty String email) {

        final EmailStructureDTO emailStructureDTO = new EmailStructureDTO();
        emailStructureDTO.setBody(EMAIL_CONFIRMATION_BODY);
        emailStructureDTO.setRecipient(email);
        emailStructureDTO.setSubject(DEFAULT_SUBJECT);
        emailStructureDTO.setSender(DEFAULT_SENDER);

        return emailStructureDTO;
    }

    public EmailLog toEntity() {

        return EmailLog.builder()
                .body(getBody())
                .sender(getSender())
                .recipients(getRecipient())
                .sendDate(LocalDateTime.now())
                .emailStatus(EmailStatus.WAITING)
                .subject(getSubject())
                .build();
    }

    public VerifyEmailAddressRequest toAws() {
        return new VerifyEmailAddressRequest().withEmailAddress(getRecipient());
    }
}
