package helloworld.service;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import helloworld.dto.EmailStructureDTO;
import helloworld.enums.TypeMail;
import helloworld.service.imp.EmailResolver;

public class EmailServiceBasic implements EmailResolver {

    private final AmazonSimpleEmailService amazonSimpleEmailService;

    public EmailServiceBasic(AmazonSimpleEmailService amazonSimpleEmailService) {
        this.amazonSimpleEmailService = amazonSimpleEmailService;
    }

    @Override
    public boolean type(TypeMail type) {
        return type.equals(TypeMail.BASIC);
    }

    public void send(final EmailStructureDTO emailStructure) {

        EmailStructureDTO.AwsFormat awsFormat = emailStructure.toAwsSend();

        final SendEmailRequest sendEmailRequest = new SendEmailRequest()
                .withSource(awsFormat.getSender())
                .withDestination(awsFormat.getDestination())
                .withMessage(awsFormat.getMessage());

        amazonSimpleEmailService.sendEmail(sendEmailRequest);
    }
}
