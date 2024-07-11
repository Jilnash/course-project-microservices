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
@ToString
@Entity
@Table(name = "hw_response")
public class HwResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false, updatable = false)
    private Long teacherId;

    @Column(nullable = false, updatable = false)
    private Long homeworkId;

    @OneToMany(mappedBy = "hwResponse")
    private List<Comment> comments;

    @CreationTimestamp
    private Date createdAt;
}