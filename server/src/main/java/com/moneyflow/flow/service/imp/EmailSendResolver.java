package com.moneyflow.flow.service.imp;

import com.moneyflow.flow.dto.EmailStructureDTO;
import com.moneyflow.flow.enums.TypeMail;
import org.apache.commons.lang3.NotImplementedException;

import java.util.List;

public interface EmailSendResolver {

    default boolean resolve(final TypeMail typeMail) {
        return typeMail.equals(getTypeMail());
    }

    void send(final EmailStructureDTO emailStructure);

    TypeMail getTypeMail();

    static void extractServiceAndSend(final List<EmailSendResolver> resolvers, final EmailStructureDTO emailStructure) {
        getEmailSendResolver(resolvers, emailStructure).send(emailStructure);
    }

    private static EmailSendResolver getEmailSendResolver(final List<EmailSendResolver> resolvers, final EmailStructureDTO emailStructure) {
        return resolvers.stream()
                .filter(it -> it.resolve(emailStructure.getTypeMail()))
                .findFirst()
                .orElseThrow(() -> new NotImplementedException("Service não implementado!"));
    }

}
