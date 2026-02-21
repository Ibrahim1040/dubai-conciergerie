package com.ibrahim.dubaiconciergerie.demo.kafka.producer;

import com.ibrahim.dubaiconciergerie.demo.kafka.event.BookingCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "app.kafka.enabled", havingValue = "true")
public class BookingEventProducer {

    private static final Logger log = LoggerFactory.getLogger(BookingEventProducer.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String topic;

    public BookingEventProducer(KafkaTemplate<String, Object> kafkaTemplate,
                                @Value("${app.kafka.topics.booking-created:booking-created}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void send(BookingCreatedEvent event) {
        log.info("KAFKA SEND => topic={}, key={}, event={}", topic, event.bookingId(), event);
        kafkaTemplate.send(topic, event.bookingId().toString(), event);
    }
}
