package com.lodecra.apiV1.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicListing;
import org.apache.kafka.common.internals.Topic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Value("${lodecra.kafka.topic1}")
    private String KAFKA_TOPIC;

    @Bean
    public NewTopic topic() {
        return TopicBuilder.name(KAFKA_TOPIC)
                .partitions(2)
                .replicas(1)
                .build();
    }

}
