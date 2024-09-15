package com.jilnash.courseservice.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.neo4j.core.schema.*;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.util.Date;
import java.util.List;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Node("Task")
public class Task {

    //fields

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String id;

    @Property("title")
    private String title;

    @Property("description")
    private String description;

    @Property("videoLink")
    private String videoLink;

    @Property("audioRequired")
    private Boolean audioRequired;

    @Property("videoRequired")
    private Boolean videoRequired;


    //audit fields

    @Property("createdBy")
    private Long createdBy;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;


    //relationships

    @Relationship(type = "CONTAINS", direction = Relationship.Direction.INCOMING)
    private Module module;

    @Relationship(type = "IS_PREREQUISITE", direction = Relationship.Direction.OUTGOING)
    private List<Task> tasks;
}
