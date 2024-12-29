package com.jilnash.courserightsservice.model;

import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "teacher_rights")
public class TeacherRights {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String courseId;

    @Column(nullable = false)
    private String teacherId;

    @ManyToOne
    private Right right;
}