package com.jilnash.hwservicesaga.aspect;

import com.jilnash.fileservicedto.dto.FileUploadRollbackDTO;
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
        List<String> fileNames = homework.getFiles().stream()
                .map(file -> "homework-" + homework.getHomeworkId() + "/" + file.getOriginalFilename())
                .toList();

        List<RollbackStage> rollbackStages = List.of(
                new RollbackStage("homework-create-rollback-topic", homework.getHomeworkId()),
                new RollbackStage("file-upload-rollback-topic",
                        new FileUploadRollbackDTO(transactionId, "course-project-homeworks", fileNames))
        );

        transactionMap.put(transactionId, new Transaction(transactionId, rollbackStages));
    }

    @Before(
            value = "execution(* com.jilnash.hwservicesaga.service.HomeworkSagaServiceImpl.setHomeworkChecked(..)) && " +
                    "args(*, *, id)",
            argNames = "id"
    )
    public void beforeSetHomeworkChecked(UUID id) {

        String transactionId = request.getHeader("X-Transaction-Id");

        List<RollbackStage> rollbackStages = List.of(
                new RollbackStage("homework-checked-rollback-topic", id)
        );

        transactionMap.put(transactionId, new Transaction(transactionId, rollbackStages));
    }

    @Before(
            value = "execution(* com.jilnash.hwservicesaga.service.HomeworkSagaServiceImpl.softDeleteHomework(..)) && " +
                    "args(*, *, id)",
            argNames = "id"
    )
    public void beforeSoftDeleteHomework(UUID id) {
        String transactionId = request.getHeader("X-Transaction-Id");

        List<RollbackStage> rollbackStages = List.of(
                new RollbackStage("homework-soft-delete-rollback-topic", id)
        );

        transactionMap.put(transactionId, new Transaction(transactionId, rollbackStages));
    }
}
