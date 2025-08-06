package com.jilnash.courserightsservice.listener;

import com.jilnash.courserightsservice.service.TeacherRightsServiceRollback;
import com.jilnash.courserightsservicedto.dto.CreateOwnerDTO;
import com.jilnash.courserightsservicedto.dto.SetRightsDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class RollbackListener {

    private final TeacherRightsServiceRollback teacherRightsServiceRollback;

    public RollbackListener(TeacherRightsServiceRollback teacherRightsServiceRollback) {
        this.teacherRightsServiceRollback = teacherRightsServiceRollback;
    }

    @KafkaListener(topics = "create-course-owner-rollback-topic", groupId = "course-rights-group")
    public void rollbackCreateCourseOwner(CreateOwnerDTO dto) {
        teacherRightsServiceRollback.createCourseOwnerRollback(dto.courseId(), dto.teacherId());
    }

    @KafkaListener(topics = "set-course-rights-rollback-topic", groupId = "course-rights-group")
    public void rollbackSetRights(SetRightsDTO dto) {
        teacherRightsServiceRollback.setRightsRollback(dto.courseId(), dto.teacherId(), dto.rights());
    }
}
