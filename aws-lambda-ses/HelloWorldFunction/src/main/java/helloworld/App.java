package helloworld;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import helloworld.config.AWSCredentialsResolver;
import helloworld.dto.EmailStructureDTO;
import helloworld.service.EmailServiceBasic;
import helloworld.service.EmailServiceVerification;
import helloworld.service.imp.EmailResolver;

/**
 * Handler for requests to Lambda function.
 */
public class App implements RequestHandler<SQSEvent, Boolean> {

    private static final List<EmailResolver> resolvers = new ArrayList<>();

    static {

        final AmazonSimpleEmailService clientService = AmazonSimpleEmailServiceClientBuilder.standard().withRegion(Regions.US_EAST_1).withCredentials(new AWSCredentialsResolver(System.getenv("accessKey"), System.getenv("secretKey"))).build();

        resolvers.add(new EmailServiceBasic(clientService));
        resolvers.add(new EmailServiceVerification(clientService));
    }

    public Boolean handleRequest(final SQSEvent sqsEvent, final Context context) {

        try {

            for (SQSEvent.SQSMessage record : sqsEvent.getRecords()) {

                final EmailStructureDTO emailStructure = getObjectMapper().readValue(record.getBody(), EmailStructureDTO.class);

                final EmailResolver serviceResolver = resolvers.stream()
                        .filter(it -> it.type(emailStructure.getTypeMail()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Service not found"));

                serviceResolver.send(emailStructure);

            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public ObjectMapper getObjectMapper() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper;
    }

}
