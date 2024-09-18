package com.epam.demo.kafka.integration;

import com.epam.demo.consumer.TaskKafkaConsumer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@Testcontainers
public class KafkaConsumerIntegrationTest {

    @Container
    static KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));

    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
    }


    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;


    @Test
    public void testKafkaConsumer() throws InterruptedException {
        String message = "Test message";
        String topic = "your-topic";

        kafkaTemplate.send(topic, message);

        // Wait for the consumer to process the message
        Thread.sleep(1000);


        // assertEquals(message, consumer.getLastReceivedMessage());
    }
}
