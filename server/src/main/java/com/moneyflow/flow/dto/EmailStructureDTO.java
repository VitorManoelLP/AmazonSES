package com.moneyflow.flow.dto;

import com.amazonaws.services.simpleemail.model.*;
import com.moneyflow.flow.domain.EmailLog;
import com.moneyflow.flow.enums.EmailStatus;
import com.moneyflow.flow.enums.TypeMail;
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
    private static final String EMAIL_CONFIRMATION_BODY = "Olá,\n\n" + "Obrigado por se cadastrar no MoneyFlow! Para continuar utilizando nossa plataforma, por favor, clique no link abaixo para confirmar o seu e-mail:\n\n" + "CONFIRMAR E-MAIL: %s\n\n" + "Se você não se cadastrou no MoneyFlow, ignore este e-mail.\n\n" + "Atenciosamente,\n" + "Equipe MoneyFlow";

    private static final String EMAIL_PASSWORD_CHANGE_BODY = "Olá,\n\nSua senha do MoneyFlow foi alterada com sucesso. Se você não realizou esta alteração, entre em contato com nosso suporte imediatamente.\n\nAtenciosamente,\nEquipe MoneyFlow";

    @Builder.Default
    private final LocalDateTime sendDate = LocalDateTime.now();
    @NotNull
    private String body;
    @NotNull
    private String subject;
    @NotNull
    private String recipient;
    @NotNull
    private String sender;
    @NotNull
    private TypeMail typeMail;

    public static EmailStructureDTO newInstanceBySystemPassword(@NotNull @NotEmpty String email) {

        final EmailStructureDTO emailStructureDTO = new EmailStructureDTO();
        emailStructureDTO.setBody(EMAIL_PASSWORD_CHANGE_BODY);
        emailStructureDTO.setRecipient(email);
        emailStructureDTO.setSubject("Confirmação de alteração de senha - MoneyFlow");
        emailStructureDTO.setSender(DEFAULT_SENDER);
        emailStructureDTO.setTypeMail(TypeMail.BASIC);

        return emailStructureDTO;
    }

    public static EmailStructureDTO newInstanceBySystem(@NotNull @NotEmpty String email) {

        final EmailStructureDTO emailStructureDTO = new EmailStructureDTO();
        emailStructureDTO.setBody(EMAIL_CONFIRMATION_BODY);
        emailStructureDTO.setRecipient(email);
        emailStructureDTO.setSubject(DEFAULT_SUBJECT);
        emailStructureDTO.setSender(DEFAULT_SENDER);
        emailStructureDTO.setTypeMail(TypeMail.VERIFICATION);

        return emailStructureDTO;
    }

    public EmailLog toEntity() {
        return EmailLog.builder().body(getBody()).sender(getSender()).recipients(getRecipient()).sendDate(LocalDateTime.now()).emailStatus(EmailStatus.WAITING).subject(getSubject()).build();
    }

    public VerifyEmailAddressRequest toAwsVerify() {
        return new VerifyEmailAddressRequest().withEmailAddress(getRecipient());
    }

    public AwsFormat toAwsSend() {

        final Destination destination = new Destination().withToAddresses(getRecipient());

        final Content subject = new Content().withData(getSubject());

        final Content textBody = new Content().withData(getBody());

        final Body body = new Body().withText(textBody);

        final Message message = new Message().withSubject(subject).withBody(body);

        return AwsFormat.builder()
                .destination(destination)
                .sender(getSender())
                .message(message)
                .build();
    }


    @Builder
    @Getter
    public static final class AwsFormat {

        private final Destination destination;
        private final String sender;
        private final Message message;

    }
}
