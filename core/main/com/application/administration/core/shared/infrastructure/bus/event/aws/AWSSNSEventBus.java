package com.application.administration.core.shared.infrastructure.bus.event.aws;

import com.application.administration.core.shared.domain.Service;
import com.application.administration.core.shared.domain.bus.event.DomainEvent;
import com.application.administration.core.shared.domain.bus.event.EventBus;
import com.application.administration.core.shared.infrastructure.config.Parameter;
import com.application.administration.core.shared.infrastructure.config.ParameterNotExist;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Service
public class AWSSNSEventBus implements EventBus {

    private final AWSSNSPublisher publisher;
    private final Parameter config;

    public AWSSNSEventBus(AWSSNSPublisher publisher, Parameter config)  {
        this.publisher = publisher;
        this.config = config;
    }

    @Override
    public void publish(List<DomainEvent> events) {
        events.forEach(this::publish);
    }

    private void publish(DomainEvent domainEvent) {
        try {
           this.publisher.publish(domainEvent, config.get("AWS_SNS_TOPIC_ARN"));
        } catch (ParameterNotExist | JsonProcessingException error) {
            error.printStackTrace();
        }
    }
}
