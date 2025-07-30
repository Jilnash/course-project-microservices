package com.jilnash.courserightsservice.listener;

import com.jilnash.courserightsservice.service.TeacherRightsService;
import com.jilnash.courserightsservicedto.dto.CheckRightsDTO;
import com.jilnash.courserightsservicedto.dto.CreateOwnerDTO;
import com.jilnash.courserightsservicedto.dto.SetRightsDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class RightsListener {

    private final TeacherRightsService teacherRightsService;

    public RightsListener(TeacherRightsService teacherRightsService) {
        this.teacherRightsService = teacherRightsService;
    }

    @KafkaListener(topics = "check-course-rights-topic", groupId = "course-rights-group")
    public void checkRights(CheckRightsDTO dto) {
        System.out.println(dto.toString());
        if (!teacherRightsService.hasRights(dto.courseId(), dto.teacherId(), dto.rights())) {
            System.out.println("Teacher does not have rights for this course");
        }
    }

    @KafkaListener(topics = "set-course-rights-topic", groupId = "course-rights-group")
    public void setRights(SetRightsDTO dto) {
        System.out.println(dto.toString());
        if (!teacherRightsService.setRights(dto.courseId(), dto.teacherId(), dto.rights())) {
            System.out.println("Failed to set rights for the teacher on the course");
        }
    }

    @KafkaListener(topics = "create-course-owner-topic", groupId = "course-rights-group")
    public void createOwner(CreateOwnerDTO dto) {
        System.out.println(dto.toString());
        if (!teacherRightsService.createCourseOwner(dto.courseId(), dto.teacherId())) {
            System.out.println("Failed to create owner for the course");
        }
    }
}
