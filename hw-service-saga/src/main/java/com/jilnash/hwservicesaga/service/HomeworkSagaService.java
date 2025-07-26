package com.jilnash.hwservicesaga.service;

import com.jilnash.hwservicedto.dto.HomeworkResponse;
import com.jilnash.hwservicesaga.dto.HomeworkCreateSagaDTO;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

public interface HomeworkSagaService {

    List<HomeworkResponse> getHomeworks(String taskId, String studentId, Boolean checked, Date createdAfter);

    HomeworkResponse getHomework(UUID id);

    void setHomeworkChecked(UUID id);

    void createHomework(HomeworkCreateSagaDTO homework);

    void softDeleteHomework(UUID id);

    void hardDeleteHomework(UUID id);
}
