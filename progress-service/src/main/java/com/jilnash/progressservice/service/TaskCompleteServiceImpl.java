package com.jilnash.progressservice.service;

import com.jilnash.progressservice.model.StudentTaskComplete;
import com.jilnash.progressservice.repo.StudentTaskCompleteRepository;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Set;

@Service
public class TaskCompleteServiceImpl implements TaskCompleteService {

    private final StudentTaskCompleteRepository studentTaskCompleteRepo;

    public TaskCompleteServiceImpl(StudentTaskCompleteRepository studentTaskCompleteRepo) {
        this.studentTaskCompleteRepo = studentTaskCompleteRepo;
    }

    @Override
    public Boolean insertTask(String taskId, List<String> completedTaskIds) {

        //get student ids, that have completed any task from the list
        Set<String> studentIds = studentTaskCompleteRepo.findStudentIdsByTaskIds(completedTaskIds);

        //adding taskId to the list of completed tasks
        List<StudentTaskComplete> studentTaskCompletes = studentIds.stream().map(studentId -> StudentTaskComplete.builder()
                        .studentId(studentId)
                        .taskId(taskId)
                        .completeDate(new Date(System.currentTimeMillis()))
                        .build())
                .toList();

        studentTaskCompleteRepo.saveAll(studentTaskCompletes);

        return true;
    }

    @Override
    public Boolean softDeleteTasks(List<String> taskIds) {
        studentTaskCompleteRepo.updateDeletedAtByTaskIdIn(new Date(System.currentTimeMillis()), taskIds);
        return true;
    }

    @Override
    public Boolean hardDeleteTasks(List<String> taskIds) {
        studentTaskCompleteRepo.deleteAllByTaskIdIn(taskIds);
        return true;
    }
}
