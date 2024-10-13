package com.ticketPing.queue_manage.infrastructure.messaging;

import static com.ticketPing.queue_manage.domain.utils.QueueTokenValueGenerator.generateTokenValue;

import com.ticketPing.queue_manage.application.service.WorkingQueueApplicationService;
import com.ticketPing.queue_manage.domain.events.OrderCompletedEvent;
import common.utils.EventSerializer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventConsumer {

    private final WorkingQueueApplicationService workingQueueService;

    @KafkaListener(topics = "order-completed", groupId = "queue-manage-group")
    public void handleOrderCompletedEvent(String message) {
        OrderCompletedEvent event = EventSerializer.deserialize(message, OrderCompletedEvent.class);
        workingQueueService.processQueueTransfer(generateTokenValue(event.userId(), event.performanceId()));
    }

}
