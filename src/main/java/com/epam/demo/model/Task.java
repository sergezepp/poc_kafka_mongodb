package com.epam.demo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = "tasks")
@Data
public class Task implements Serializable {

    @Id
    private String id;
    private String title;
    private String description;
    private String status;
    private String userId;


}