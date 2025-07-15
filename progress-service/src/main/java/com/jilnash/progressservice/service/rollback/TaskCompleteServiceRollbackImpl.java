package com.jilnash.progressservice.service.rollback;

import com.jilnash.progressservice.repo.StudentTaskCompleteRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskCompleteServiceRollbackImpl implements TaskCompleteServiceRollback {

    private final StudentTaskCompleteRepository studentTaskCompleteRepo;

    public TaskCompleteServiceRollbackImpl(StudentTaskCompleteRepository studentTaskCompleteRepo) {
        this.studentTaskCompleteRepo = studentTaskCompleteRepo;
    }

    @Override
    public Boolean insertTaskRollback(String taskId) {
        studentTaskCompleteRepo.deleteAllByTaskIdIn(List.of(taskId));
        return true;
    }

    @Override
    public Boolean softDeleteTasksRollback(List<String> taskIds) {
        studentTaskCompleteRepo.updateDeletedAtByTaskIdIn(null, taskIds);
        return true;
    }
}
