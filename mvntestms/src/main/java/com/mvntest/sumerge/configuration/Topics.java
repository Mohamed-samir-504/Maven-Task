package com.mvntest.sumerge.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.mvntest.sumerge.common.Constants.USER_TEST_TOPIC;

@Configuration
public class Topics {

    @Value("${kafka.number.of.partitions}")
    private int numPartitions;

    @Value("${kafka.number.of.replication}")
    private short replicationFactor;

    @Bean
    public NewTopic test() {
        return new NewTopic(USER_TEST_TOPIC, numPartitions, replicationFactor);
    }
}
