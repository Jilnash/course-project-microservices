package com.jilnash.progressservice.service.rollback;

import com.jilnash.progressservice.repo.StudentTaskCompleteRepository;
import org.springframework.stereotype.Service;

@Service
public class StudentProgressServiceRollabackImpl implements StudentProgressServiceRollback {

    private final StudentTaskCompleteRepository studentTaskCompleteRepo;

    public StudentProgressServiceRollabackImpl(StudentTaskCompleteRepository studentTaskCompleteRepo) {
        this.studentTaskCompleteRepo = studentTaskCompleteRepo;
    }

    @Override
    public Boolean addStudentTaskCompleteRollback(String studentId, String taskId) {

        studentTaskCompleteRepo.deleteByStudentIdAndTaskId(studentId, taskId);
        return null;
    }
}
