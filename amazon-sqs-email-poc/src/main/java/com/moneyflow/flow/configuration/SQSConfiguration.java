package com.moneyflow.flow.configuration;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.messaging.config.QueueMessageHandlerFactory;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.annotation.support.PayloadMethodArgumentResolver;

import java.util.Collections;

@Configuration
public class SQSConfiguration {

    @Value("${aws.accessKey}")
    private String accessKey;

    @Value("${aws.secretKey}")
    private String secretKey;

    @Bean
    public AmazonSQSAsync amazonSQS() {
        return AmazonSQSAsyncClientBuilder
                .standard()
                .withRegion(Regions.US_EAST_1)
                .withCredentials(new AWSCredentialsResolver(accessKey, secretKey))
                .build();
    }

    @Bean
    public QueueMessagingTemplate queueMessagingTemplate(final AmazonSQSAsync amazonSQSAsync) {
        return new QueueMessagingTemplate(amazonSQSAsync);
    }

    @Bean
    public QueueMessageHandlerFactory queueMessageHandlerFactory(
            final ObjectMapper mapper, final AmazonSQSAsync amazonSQSAsync){

        final QueueMessageHandlerFactory queueHandlerFactory = new QueueMessageHandlerFactory();
        queueHandlerFactory.setAmazonSqs(amazonSQSAsync);
        queueHandlerFactory.setArgumentResolvers(Collections
                .singletonList(new PayloadMethodArgumentResolver(jackson2MessageConverter(mapper))));

        return queueHandlerFactory;
    }

    private MessageConverter jackson2MessageConverter(final ObjectMapper mapper){
        final MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setStrictContentTypeMatch(false);
        converter.setObjectMapper(mapper);
        return converter;
    }

}
