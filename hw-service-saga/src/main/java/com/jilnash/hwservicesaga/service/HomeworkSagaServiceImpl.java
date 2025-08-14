package com.jilnash.hwservicesaga.service;

import com.jilnash.courseaccessservicedto.dto.CheckAccessDTO;
import com.jilnash.courserightsservicedto.dto.CheckRightsDTO;
import com.jilnash.hwservicedto.dto.HomeworkResponse;
import com.jilnash.hwservicesaga.FileServiceClient;
import com.jilnash.hwservicesaga.dto.HomeworkCreateSagaDTO;
import com.jilnash.hwservicesaga.mapper.HomeworkMapper;
import com.jilnash.progressservicedto.dto.CheckStudentCompletedTasks;
import com.jilnash.taskrequirementsservicedto.dto.CheckOfRequirements;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HomeworkSagaServiceImpl implements HomeworkSagaService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final FileServiceClient fileServiceClient;

    private final HomeworkMapper homeworkMapper;

    private final HttpServletRequest request;

    @Override
    public List<HomeworkResponse> getHomeworks(String taskId, String studentId, Boolean checked, Date createdAfter) {
        //todo: check user permissions
        return List.of();
    }

    @Override
    public HomeworkResponse getHomework(UUID id) {
        return null;
    }

    @Override
    public void setHomeworkChecked(String courseId, String teacherId, UUID id) {

        String transactionId = request.getHeader("X-Transaction-Id");
        kafkaTemplate.send("check-course-rights-topic",
                new CheckRightsDTO(transactionId, courseId, teacherId, Set.of("RESPOND")));

        kafkaTemplate.send("homework-checked-topic", id);
    }

    @Override
    public void createHomework(HomeworkCreateSagaDTO homework) {

        String transactionId = request.getHeader("X-Transaction-Id");
        // validate request
        kafkaTemplate.send("check-student-already-completed-tasks-topic",
                new CheckStudentCompletedTasks(transactionId, homework.getStudentId(), List.of(homework.getTaskId())));
        kafkaTemplate.send("check-hw-requirements-topic",
                new CheckOfRequirements(transactionId, homework.getTaskId(),
                        homework.getFiles().stream().map(MultipartFile::getContentType).toList()));
        kafkaTemplate.send("check-course-access-topic",
                new CheckAccessDTO(transactionId, homework.getCourseId(), homework.getStudentId()));

        // commit transaction
        fileServiceClient.uploadFileAsync(homework.getHomeworkId(), homework.getFiles());
        kafkaTemplate.send("homework-create-topic", homeworkMapper.homeworkCreateDTO(homework));
    }

    @Override
    public void softDeleteHomework(String courseId, String teacherId, UUID id) {

        String transactionId = request.getHeader("X-Transaction-Id");
        kafkaTemplate.send("check-course-rights-topic",
                new CheckRightsDTO(transactionId, courseId, teacherId, Set.of("DELETE")));

        kafkaTemplate.send("homework-soft-delete-topic", id);
    }

    @Override
    public void hardDeleteHomework(String courseId, String teacherId, UUID id) {

        String transactionId = request.getHeader("X-Transaction-Id");
        kafkaTemplate.send("check-course-rights-topic",
                new CheckRightsDTO(transactionId, courseId, teacherId, Set.of("DELETE")));

        kafkaTemplate.send("homework-hard-delete-topic", id);
    }
}
