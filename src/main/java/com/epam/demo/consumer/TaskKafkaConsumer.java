package com.epam.demo.consumer;

import com.epam.demo.model.Task;
import com.epam.demo.service.TaskService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TaskKafkaConsumer {

    private final TaskService taskService;

    public TaskKafkaConsumer(TaskService taskService) {
        this.taskService = taskService;
    }

    @KafkaListener(topics = "${kafka.topic.task}", containerFactory = "taskKafkaListenerContainerFactory")
    public void consumeTask(Task task) {
        System.out.println("Received Task: " + task);
        // Process the received task
        taskService.createTask(task);
    }
}