package com.jilnash.homeworkservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

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

    @Column
    private Date deletedAt;

    @OneToMany(mappedBy = "homeworkId", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private List<HomeworkFile> hwFiles;
}