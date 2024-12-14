package com.jilnash.hwresponseservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Date;
import java.util.List;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "comments")
@Entity
@Table(name = "hw_response")
public class HwResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false, updatable = false)
    private String teacherId;

    @Column(nullable = false, updatable = false)
    private Long homeworkId;

    @OneToMany(mappedBy = "hwResponse")
    private List<Comment> comments;

    @Column(nullable = false, updatable = false)
    private Boolean isCorrect;

    @CreationTimestamp
    @Column(updatable = false)
    private Date createdAt;
}