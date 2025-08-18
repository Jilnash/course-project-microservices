package com.jilnash.courseservicesaga.aspect;

import com.jilnash.courseservicedto.dto.course.CourseCreateDTO;
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
                    "&& args(*, courseId, *)",
            argNames = "courseId"
    )
    public void beforeCourseUpdateName(String courseId) {
        String transactionId = request.getHeader("X-Transaction-Id");
        List<RollbackStage> rollbackStages = List.of(
                new RollbackStage("course-name-update-rollback-topic", courseId)
        );

        transactionMap.putIfAbsent(transactionId, new Transaction(transactionId, rollbackStages));
    }

    @Before(
            value = "execution(* com.jilnash.courseservicesaga.service.course.CourseServiceSagaImpl.updateCourseDescription(..))" +
                    "&& args(*, courseId, *)",
            argNames = "courseId"
    )
    public void beforeCourseUpdateDescription(String courseId) {

        String transactionId = request.getHeader("X-Transaction-Id");
        List<RollbackStage> rollbackStages = List.of(
                new RollbackStage("course-description-update-rollback-topic", courseId)
        );

        transactionMap.putIfAbsent(transactionId, new Transaction(transactionId, rollbackStages));
    }

    @Before(
            value = "execution(* com.jilnash.courseservicesaga.service.course.CourseServiceSagaImpl.updateCourseDuration(..))" +
                    "&& args(*, courseId, *)",
            argNames = "courseId"
    )
    public void beforeCourseUpdateDuration(String courseId) {

        String transactionId = request.getHeader("X-Transaction-Id");
        List<RollbackStage> rollbackStages = List.of(
                new RollbackStage("course-duration-update-rollback-topic", courseId)
        );

        transactionMap.putIfAbsent(transactionId, new Transaction(transactionId, rollbackStages));
    }

    @Before(
            value = "execution(* com.jilnash.courseservicesaga.service.course.CourseServiceSagaImpl.softDeleteCourse(..))" +
                    "&& args(*, courseId)",
            argNames = "courseId"
    )
    public void beforeCourseSoftDelete(String courseId) {

        String transactionId = request.getHeader("X-Transaction-Id");
        List<RollbackStage> rollbackStages = List.of(
                new RollbackStage("course-soft-delete-rollback-topic", courseId)
        );

        transactionMap.putIfAbsent(transactionId, new Transaction(transactionId, rollbackStages));
    }
}
