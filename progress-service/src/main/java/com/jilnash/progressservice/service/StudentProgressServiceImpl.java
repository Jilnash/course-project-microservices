package com.jilnash.progressservice.service;

import com.jilnash.progressservice.model.StudentTaskComplete;
import com.jilnash.progressservice.repo.StudentTaskCompleteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentProgressServiceImpl implements StudentProgressService {

    private final StudentTaskCompleteRepository studentTaskCompleteRepo;

    public StudentProgressServiceImpl(StudentTaskCompleteRepository studentTaskCompleteRepo) {
        this.studentTaskCompleteRepo = studentTaskCompleteRepo;
    }

    @Override
    public List<String> getStudentCompletedTaskIds(String studentId) {
        return studentTaskCompleteRepo.findTaskIdsByStudentId(studentId);
    }

    @Override
    public Boolean getIfStudentCompletedTasks(String studentId, List<String> taskIds) {
        return taskIds.size() == studentTaskCompleteRepo
                .countByStudentIdAndTaskIdsAndDeletedAtIsNull(studentId, taskIds);
    }

    @Override
    public Boolean addStudentTaskComplete(String studentId, String taskId) {
        studentTaskCompleteRepo.save(
                StudentTaskComplete.builder()
                        .studentId(studentId)
                        .taskId(taskId)
                        .completeDate(new java.sql.Date(System.currentTimeMillis()))
                        .deletedAt(null)
                        .build()
        );
        return null;
    }
}
