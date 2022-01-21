package com.application.administration.core.shared.infrastructure.bus.event.aws;

import com.application.administration.core.shared.domain.Service;
import com.application.administration.core.shared.domain.bus.event.DomainEvent;
import com.application.administration.core.shared.infrastructure.bus.event.DomainEventJsonSerializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.MessageAttributeValue;
import software.amazon.awssdk.services.sns.model.PublishRequest;

import java.util.HashMap;
import java.util.Map;

@Service
public class AWSSNSPublisher {

    private final SnsClient snsClient;

    public AWSSNSPublisher(SnsClient snsClient) {
        this.snsClient = snsClient;
    }

    public void publish(DomainEvent domainEvent, String topicArn) throws JsonProcessingException {
        Map<String, MessageAttributeValue> attributes = new HashMap<>();
        attributes.put("event_type", MessageAttributeValue.builder()
                .dataType("String")
                .stringValue(domainEvent.eventName())
                .build());
        String serializedDomainEvent = DomainEventJsonSerializer.serialize(domainEvent);
        PublishRequest request = PublishRequest.builder()
                .topicArn(topicArn)
                .message(serializedDomainEvent)
                .messageAttributes(attributes)
                .build();
        this.snsClient.publish(request);
    }
}
