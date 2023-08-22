package com.moneyflow.flow.configuration;

import com.moneyflow.flow.domain.EmailLog;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqpQueueConfig {

    @Bean
    public Queue emailQueue() {
        return new Queue(EmailLog.DEFAULT_QUEUE, true);
    }

}
