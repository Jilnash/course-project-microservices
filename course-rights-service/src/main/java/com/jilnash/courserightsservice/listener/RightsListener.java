package com.jilnash.courserightsservice.listener;

import com.jilnash.courserightsservice.service.TeacherRightsService;
import com.jilnash.courserightsservicedto.dto.CheckRightsDTO;
import com.jilnash.courserightsservicedto.dto.CreateOwnerDTO;
import com.jilnash.courserightsservicedto.dto.SetRightsDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class RightsListener {

    private final TeacherRightsService teacherRightsService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public RightsListener(TeacherRightsService teacherRightsService,
                          KafkaTemplate<String, String> kafkaTemplate) {
        this.teacherRightsService = teacherRightsService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "check-course-rights-topic", groupId = "course-rights-group")
    public void checkRights(CheckRightsDTO dto) {
        System.out.println(dto.toString());
        if (!teacherRightsService.hasRights(dto.courseId(), dto.teacherId(), dto.rights()))
            kafkaTemplate.send("rollback-topic", dto.transactionId());
    }

    @KafkaListener(topics = "set-course-rights-topic", groupId = "course-rights-group")
    public void setRights(SetRightsDTO dto) {
        System.out.println(dto.toString());
        try {
            teacherRightsService.setRights(dto.courseId(), dto.teacherId(), dto.rights());
        } catch (Exception e) {
            kafkaTemplate.send("rollback-topic", dto.transactionId());
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
