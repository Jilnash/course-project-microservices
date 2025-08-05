package com.jilnash.courseservicesaga.aspect;

import com.jilnash.courseservicedto.dto.course.CourseCreateDTO;
import com.jilnash.courseservicedto.dto.course.CourseUpdateDescriptionDTO;
import com.jilnash.courseservicedto.dto.course.CourseUpdateDurationDTO;
import com.jilnash.courseservicedto.dto.course.CourseUpdateNameDTO;
import com.jilnash.courseservicesaga.transaction.RollbackStage;
import com.jilnash.courseservicesaga.transaction.Transaction;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Aspect
@Component
public class CourseServiceAspect {

    private final HttpServletRequest request;

    private final Map<String, Transaction> transactionMap;

    public CourseServiceAspect(HttpServletRequest request,
                               Map<String, Transaction> transactionMap) {
        this.request = request;
        this.transactionMap = transactionMap;
    }

    @Before(
            value = "execution(* com.jilnash.courseservicesaga.service.course.CourseServiceSagaImpl.createCourse(..))" +
                    "&& args(dto)",
            argNames = "dto"
    )
    public void beforeCourseCreate(CourseCreateDTO dto) {

        String transactionId = request.getHeader("X-Transaction-Id");
        List<RollbackStage> rollbackStages = List.of(
//                new RollbackStage("set-rights-rollback-topic", dto.getId()),
                new RollbackStage("course-create-rollback-topic", dto.getId())
        );

        transactionMap.putIfAbsent(transactionId, new Transaction(transactionId, rollbackStages));
    }

    @Before(
            value = "execution(* com.jilnash.courseservicesaga.service.course.CourseServiceSagaImpl.updateCourseName(..))" +
                    "&& args(teacherId, courseId, name)",
            argNames = "teacherId,courseId,name"
    )
    public void beforeCourseUpdateName(String teacherId, String courseId, String name) {
        String transactionId = request.getHeader("X-Transaction-Id");
        List<RollbackStage> rollbackStages = List.of(
                new RollbackStage("course-name-update-rollback-topic",
                        new CourseUpdateNameDTO(transactionId, courseId, "Prev name")
                )
        );

        transactionMap.putIfAbsent(transactionId, new Transaction(transactionId, rollbackStages));
    }

    @Before(
            value = "execution(* com.jilnash.courseservicesaga.service.course.CourseServiceSagaImpl.updateCourseDescription(..))" +
                    "&& args(teacherId, courseId, description)",
            argNames = "teacherId,courseId,description"
    )
    public void beforeCourseUpdateDescription(String teacherId, String courseId, String description) {

        String transactionId = request.getHeader("X-Transaction-Id");
        List<RollbackStage> rollbackStages = List.of(
                new RollbackStage("course-description-update-rollback-topic",
                        new CourseUpdateDescriptionDTO(transactionId, courseId, "Prev description")
                )
        );

        transactionMap.putIfAbsent(transactionId, new Transaction(transactionId, rollbackStages));
    }

    @Before(
            value = "execution(* com.jilnash.courseservicesaga.service.course.CourseServiceSagaImpl.updateCourseDuration(..))" +
                    "&& args(teacherId, courseId, duration)",
            argNames = "teacherId,courseId,duration"
    )
    public void beforeCourseUpdateDuration(String teacherId, String courseId, String duration) {

        String transactionId = request.getHeader("X-Transaction-Id");
        List<RollbackStage> rollbackStages = List.of(
                new RollbackStage("course-duration-update-rollback-topic",
                        new CourseUpdateDurationDTO(transactionId, courseId, "Prev duration")
                )
        );

        transactionMap.putIfAbsent(transactionId, new Transaction(transactionId, rollbackStages));
    }

    @Before(
            value = "execution(* com.jilnash.courseservicesaga.service.course.CourseServiceSagaImpl.softDeleteCourse(..))" +
                    "&& args(teacherId, courseId)",
            argNames = "teacherId,courseId"
    )
    public void beforeCourseSoftDelete(String teacherId, String courseId) {

        String transactionId = request.getHeader("X-Transaction-Id");
        List<RollbackStage> rollbackStages = List.of(
                new RollbackStage("course-soft-delete-rollback-topic", courseId)
        );

        transactionMap.putIfAbsent(transactionId, new Transaction(transactionId, rollbackStages));
    }
}
