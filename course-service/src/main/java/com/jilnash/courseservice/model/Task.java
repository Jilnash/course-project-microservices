package com.jilnash.courseservice.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jilnash.courseservice.serializer.TaskListSerializer;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.neo4j.core.schema.*;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Node("Task")
public class Task implements Serializable {

    //fields

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String id;

    @Property("title")
    private String title;

    @Property("description")
    private String description;

    @Property("videoFileName")
    private String videoFileName;

    @Property("isPublic")
    private Boolean isPublic;

    @Property("hwPostingInterval")
    private Integer hwPostingInterval;

    //audit fields

    @Property("createdBy")
    private String createdBy;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;

    @Property("deletedAt")
    private Date deletedAt;

    //relationships

    @Relationship(type = "CONTAINS", direction = Relationship.Direction.INCOMING)
    private Module module;

    @JsonSerialize(using = TaskListSerializer.class)
    @Relationship(type = "IS_PREREQUISITE", direction = Relationship.Direction.OUTGOING)
    private Set<Task> successors;

    @JsonSerialize(using = TaskListSerializer.class)
    @Relationship(type = "IS_PREREQUISITE", direction = Relationship.Direction.INCOMING)
    private Set<Task> prerequisites;
}
