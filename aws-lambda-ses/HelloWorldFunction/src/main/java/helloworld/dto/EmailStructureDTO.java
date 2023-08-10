package helloworld.dto;

import com.amazonaws.services.simpleemail.model.*;
import helloworld.enums.TypeMail;

import java.io.Serializable;
import java.time.LocalDateTime;

public class EmailStructureDTO implements Serializable {

    private final LocalDateTime sendDate = LocalDateTime.now();

    private String body;

    private String subject;

    private String recipient;

    private String sender;

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

        return new AwsFormat(destination, sender, message);
    }

    public LocalDateTime getSendDate() {
        return sendDate;
    }

    public String getBody() {
        return body;
    }

    public String getSubject() {
        return subject;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getSender() {
        return sender;
    }

    public TypeMail getTypeMail() {
        return typeMail;
    }

    public static final class AwsFormat {

        private final Destination destination;
        private final String sender;
        private final Message message;

        public AwsFormat(Destination destination, String sender, Message message) {
            this.destination = destination;
            this.sender = sender;
            this.message = message;
        }

        public Destination getDestination() {
            return destination;
        }

        public Message getMessage() {
            return message;
        }

        public String getSender() {
            return sender;
        }

    }
}
