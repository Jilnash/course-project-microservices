package com.jilnash.hwservicesaga.aspect;

import com.jilnash.hwservicesaga.dto.HomeworkCreateSagaDTO;
import com.jilnash.hwservicesaga.transaction.RollbackStage;
import com.jilnash.hwservicesaga.transaction.Transaction;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Aspect
@Component
public class HomeworkServiceAspect {

    private final Map<String, Transaction> transactionMap;

    private final HttpServletRequest request;

    public HomeworkServiceAspect(Map<String, Transaction> transactionMap,
                                 HttpServletRequest request) {
        this.transactionMap = transactionMap;
        this.request = request;
    }

    @Before(
            value = "execution(* com.jilnash.hwservicesaga.service.HomeworkSagaServiceImpl.createHomework(..)) && " +
                    "args(homework)",
            argNames = "homework"
    )
    public void beforeCreateHomework(HomeworkCreateSagaDTO homework) {

        String transactionId = request.getHeader("X-Transaction-Id");

        List<RollbackStage> rollbackStages = List.of(
                new RollbackStage("homework-create-rollback-topic", homework.getHomeworkId())
                //todo: rollback file upload
        );
        System.out.println("Transaction ID: " + transactionId);

        transactionMap.put(transactionId, new Transaction(transactionId, rollbackStages));
    }

    @Before(
            value = "execution(* com.jilnash.hwservicesaga.service.HomeworkSagaServiceImpl.setHomeworkChecked(..)) && " +
                    "args(courseId, teacherId, id)",
            argNames = "courseId, teacherId, id"
    )
    public void beforeSetHomeworkChecked(String courseId, String teacherId, UUID id) {

        String transactionId = request.getHeader("X-Transaction-Id");

        List<RollbackStage> rollbackStages = List.of(
                new RollbackStage("homework-checked-rollback-topic", id)
                //todo: check
        );

        transactionMap.put(transactionId, new Transaction(transactionId, rollbackStages));
    }

    @Before(
            value = "execution(* com.jilnash.hwservicesaga.service.HomeworkSagaServiceImpl.softDeleteHomework(..)) && " +
                    "args(courseId, teacherId, id)",
            argNames = "courseId, teacherId, id"
    )
    public void beforeSoftDeleteHomework(String courseId, String teacherId, UUID id) {
        String transactionId = request.getHeader("X-Transaction-Id");

        List<RollbackStage> rollbackStages = List.of(
                new RollbackStage("homework-soft-delete-rollback-topic", id)
        );

        transactionMap.put(transactionId, new Transaction(transactionId, rollbackStages));
    }
}
