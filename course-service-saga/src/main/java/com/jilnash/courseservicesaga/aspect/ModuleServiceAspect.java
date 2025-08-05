package com.jilnash.courseservicesaga.aspect;

import com.jilnash.courseservicedto.dto.module.ModuleUpdateNameDTO;
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
public class ModuleServiceAspect {

    private final HttpServletRequest request;

    private final Map<String, Transaction> transactionMap;

    public ModuleServiceAspect(HttpServletRequest request,
                               Map<String, Transaction> transactionMap) {
        this.request = request;
        this.transactionMap = transactionMap;
    }

    @Before(value =
            "execution(* com.jilnash.courseservicesaga.service.module.ModuleServiceSagaImpl.updateModuleName(..))" +
                    "&& args(teacherId, courseId, id, name)",
            argNames = "teacherId,courseId,id,name"
    )
    public void beforeModuleUpdateName(String teacherId, String courseId, String id, String name) {

        String transactionId = request.getHeader("X-Transaction-Id");
        List<RollbackStage> rollbackStages = List.of(
                new RollbackStage(
                        "module-update-name-rollback-topic",
                        new ModuleUpdateNameDTO(courseId, id, "Prev name")
                )
        );

        transactionMap.putIfAbsent(transactionId, new Transaction(transactionId, rollbackStages));
    }
}
