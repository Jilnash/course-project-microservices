package com.jilnash.hwresponseservicesaga.aspect;

import com.jilnash.hwresponseservicedto.dto.ResponseCreateDTO;
import com.jilnash.hwresponseservicesaga.transaction.RollbackStage;
import com.jilnash.hwresponseservicesaga.transaction.Transaction;
import com.jilnash.progressservicedto.dto.AddStudentTaskCompleteDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Aspect
@Component
public class ResponseServiceAspect {

    private final Map<String, Transaction> transactionMap;

    private final HttpServletRequest request;

    public ResponseServiceAspect(Map<String, Transaction> transactionMap,
                                 HttpServletRequest request) {
        this.transactionMap = transactionMap;
        this.request = request;
    }

    @Before(
            value = "execution(* com.jilnash.hwresponseservicesaga.service.ResponseServiceSagaImpl.createResponse(..)) && " +
                    "args(response)",
            argNames = "response"
    )
    public void beforeCreateResponse(ResponseCreateDTO response) {


        String transactionId = request.getHeader("X-Transaction-Id");
        List<RollbackStage> stages = List.of(
                new RollbackStage("homework-checked-roolback-topic", UUID.fromString(response.getHwId())),
                new RollbackStage("response-create-rollback-topic", response.getId()),
                new RollbackStage("add-student-task-complete-rollback-topic",
                        new AddStudentTaskCompleteDTO(transactionId, response.getStudentId(), response.getTaskId()))
        );

        transactionMap.putIfAbsent(transactionId, new Transaction(transactionId, stages));
    }

    @Before(
            value = "execution(* com.jilnash.hwresponseservicesaga.service.ResponseServiceSagaImpl.updateResponse(..)) && " +
                    "args(response)",
            argNames = "response"
    )
    public void beforeUpdateResponse(ResponseCreateDTO response) {

        String transactionId = request.getHeader("X-Transaction-Id");
        List<RollbackStage> stages = List.of(
                new RollbackStage("response-update-rollback-topic", response.getId())
        );

        transactionMap.putIfAbsent(transactionId, new Transaction(transactionId, stages));
    }

    @Before(
            value = "execution(* com.jilnash.hwresponseservicesaga.service.ResponseServiceSagaImpl.softDeleteResponse(..)) && " +
                    "args(courseId, teacherId, responseId)",
            argNames = "courseId, teacherId, responseId"
    )
    public void beforeSoftDeleteResponse(String courseId, String teacherId, String responseId) {

        String transactionId = request.getHeader("X-Transaction-Id");
        List<RollbackStage> stages = List.of(
                new RollbackStage("response-soft-delete-rollback-topic", responseId),
                new RollbackStage("soft-delete-progress-rollback-topic", List.of())
        );

        transactionMap.putIfAbsent(transactionId, new Transaction(transactionId, stages));
    }
}
