package com.jilnash.courseservice.model;

import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.neo4j.core.schema.*;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.util.Date;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Node("Module")
public class Module {

    //fields

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String id;

    @Property("name")
    private String name;

    @Property("description")
    private String description;


    //audit fields

    @CreatedBy
    @Property("createdBy")
    private Long createdBy;

    @CreatedDate
    @Property("createdAt")
    private Date createdAt;

    @LastModifiedDate
    @Property("updatedAt")
    private Date updatedAt;

    //relationships
    @Relationship(type = "CONTAINS", direction = Relationship.Direction.INCOMING)
    private Course course;
}
