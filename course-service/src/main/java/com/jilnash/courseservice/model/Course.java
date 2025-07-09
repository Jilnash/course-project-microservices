package com.jilnash.courseservice.model;

import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.io.Serializable;
import java.util.Date;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Node("Course")
public class Course implements Serializable {

    //fields

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String id;

    @Property("name")
    private String name;

    @Property("description")
    private String description;

    @Property("duration")
    private String duration;

    //audit fields

    @CreatedBy
    @Property("createdBy")
    private String createdBy;

    @CreatedDate
    @Property("createdAt")
    private Date createdAt;

    @LastModifiedDate
    @Property("updatedAt")
    private Date updatedAt;

    private Date deletedAt;
}
