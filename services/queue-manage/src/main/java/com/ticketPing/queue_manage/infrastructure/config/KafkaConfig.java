package com.ticketPing.queue_manage.infrastructure.config;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import java.util.Map;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.redisson.client.protocol.QueueCommand;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
@EnableKafka
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String SERVER;

    @Bean
    public KafkaTemplate<String, QueueCommand> reactiveKafkaTemplate() {

        return new KafkaTemplate<>(
                new DefaultKafkaProducerFactory<>(
                        Map.of(
                                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, SERVER,
                                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class,
                                ProducerConfig.ACKS_CONFIG, "all"
                        )
                )
        );
    }

}
