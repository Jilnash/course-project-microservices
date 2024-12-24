package com.jilnash.homeworkservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "homework")
public class Homework {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false, updatable = false)
    private String studentId;

    @Column(nullable = false, updatable = false)
    private String taskId;

    private String audioLink;

    private String videoLink;

    private String imageLink;

    private Boolean checked = false;

    @Column(updatable = false)
    @CreationTimestamp
    private Date createdAt;

    @Transient
    @JsonIgnore
    private MultipartFile imageFile;

    @Transient
    @JsonIgnore
    private MultipartFile audioFile;

    @Transient
    @JsonIgnore
    private MultipartFile videoFile;
}