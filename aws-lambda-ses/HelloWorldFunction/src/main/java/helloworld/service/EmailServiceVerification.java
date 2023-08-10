package helloworld.service;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.VerifyEmailAddressRequest;
import helloworld.dto.EmailStructureDTO;
import helloworld.enums.TypeMail;
import helloworld.service.imp.EmailResolver;

public class EmailServiceVerification implements EmailResolver {

    private final AmazonSimpleEmailService amazonSimpleEmailService;

    public EmailServiceVerification(AmazonSimpleEmailService amazonSimpleEmailService) {
        this.amazonSimpleEmailService = amazonSimpleEmailService;
    }

    @Override
    public boolean type(TypeMail type) {
        return type.equals(TypeMail.VERIFICATION);
    }

    public void send(final EmailStructureDTO emailStructure) {

        final VerifyEmailAddressRequest aws = emailStructure.toAwsVerify();

        amazonSimpleEmailService.verifyEmailAddress(aws);
    }

}
