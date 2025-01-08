package com.jilnash.homeworkservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.util.List;
import java.util.UUID;


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
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(nullable = false, updatable = false)
    private String studentId;

    @Column(nullable = false, updatable = false)
    private String taskId;

    @Column(nullable = false)
    private int attempt;

    private Boolean checked = false;

    @Column(updatable = false)
    @CreationTimestamp
    private Date createdAt;

    @Transient
    @JsonIgnore
    private List<MultipartFile> files;
}