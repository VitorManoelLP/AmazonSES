package com.moneyflow.flow.dto;

import com.amazonaws.services.simpleemail.model.*;
import com.moneyflow.flow.enums.TypeMail;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter(AccessLevel.PRIVATE)
@ToString
public class EmailStructureDTO implements Serializable {

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
