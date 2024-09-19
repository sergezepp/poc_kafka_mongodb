package com.epam.demo.kafka.integration;

import com.epam.demo.model.Task;
import com.epam.demo.producer.TaskKafkaProducer;
import com.epam.demo.service.TaskService;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private TaskService taskService;


    @Autowired
    private TaskKafkaProducer producer;

    @Test
    public void testKafkaConsumer() throws InterruptedException {
        String message = "{\n" +
                "  \"id\": \"12345\",\n" +
                "  \"title\": \"Test Task\",\n" +
                "  \"description\": \"This is a sample task for testing purposes.\",\n" +
                "  \"status\": \"TODO\",\n" +
                "  \"userId\": \"user123\"\n" +
                "}";
        String topic = "task-topic";

        kafkaTemplate.send(topic, message);

        // Wait for the consumer to process the message
        Thread.sleep(1000);

        var task = taskService.getTaskById("12345");

        assertNotNull(task);
    }

    @Test
    public void testKafkaProducer() throws InterruptedException {

        Task task = new Task();
        String topic = "task-topic";

        task.setDescription("Test Task");
        task.setId("9999");
        task.setStatus("TODO");
        task.setUserId("user1234");

        producer.sendMessage(topic, task);
        // Wait for the consumer to process the message
        Thread.sleep(1000);

        var taskResult = taskService.getTaskById("9999");

        assertNotNull(taskResult);
    }
}
