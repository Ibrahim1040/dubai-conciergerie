package com.ibrahim.dubaiconciergerie.demo.kafka.consumer;

import com.ibrahim.dubaiconciergerie.demo.kafka.event.BookingCreatedEvent;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "app.kafka.enabled", havingValue = "true")
public class BookingEventConsumer {

    @KafkaListener(topics = "${app.kafka.topics.booking-created}", groupId = "dubai")
    public void onMessage(BookingCreatedEvent event) {
        System.out.println("✅ RECU KAFKA => " + event);
    }
}

