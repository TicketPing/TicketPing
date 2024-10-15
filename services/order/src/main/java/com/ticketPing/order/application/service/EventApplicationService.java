package com.ticketPing.order.application.service;

import com.ticketPing.order.domain.events.OrderCompletedEvent;
import com.ticketPing.order.domain.events.OrderTopic;
import common.utils.EventSerializer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventApplicationService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishOrderCompletedEvent(OrderCompletedEvent event) {
        kafkaTemplate.send(OrderTopic.COMPLETED.getTopic(), EventSerializer.serialize(event));
    }

}
