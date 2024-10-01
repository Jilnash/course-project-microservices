package com.jilnash.courserightsservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    @Column(nullable = false)
    private Boolean edit = false;

    @Column(nullable = false)
    private Boolean delete = false;

    @Column(nullable = false)
    private Boolean add = false;

    @Column(nullable = false)
    private Boolean manageTeachers = false;


    public TeacherRights(String courseId, String teacherId, List<String> rights) {
        this.courseId = courseId;
        this.teacherId = teacherId;

        rights.forEach(right -> {
            switch (right) {
                case "edit":
                    this.edit = true;
                    break;
                case "delete":
                    this.delete = true;
                    break;
                case "add":
                    this.add = true;
                    break;
                case "manageTeachers":
                    this.manageTeachers = true;
                    break;
            }
        });
    }
}