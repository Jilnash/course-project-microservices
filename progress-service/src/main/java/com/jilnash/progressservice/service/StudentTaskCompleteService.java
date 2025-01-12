package com.jilnash.progressservice.service;

import com.jilnash.progressservice.model.StudentTaskComplete;
import com.jilnash.progressservice.repo.StudentTaskCompleteRepository;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.HashSet;
import java.util.List;

@Service
public class StudentTaskCompleteService {

    private final StudentTaskCompleteRepository studentTaskCompleteRepo;

    public StudentTaskCompleteService(StudentTaskCompleteRepository studentTaskCompleteRepo) {
        this.studentTaskCompleteRepo = studentTaskCompleteRepo;
    }

    public List<String> getCompletedTaskIds(String studentId) {
        return studentTaskCompleteRepo.findTaskIdsByStudentId(studentId);
    }

    public Boolean isTaskComplete(String studentId, String taskId) {
        return studentTaskCompleteRepo.existsByStudentIdAndTaskId(studentId, taskId);
    }

    public Boolean areTasksCompleted(String studentId, List<String> taskIds) {
        return new HashSet<>(getCompletedTaskIds(studentId)).containsAll(taskIds);
    }

    public Boolean completeTask(String studentId, String taskId) {
        studentTaskCompleteRepo.save(
                StudentTaskComplete.builder()
                        .studentId(studentId)
                        .taskId(taskId)
                        .completeDate(new Date(System.currentTimeMillis()))
                        .build()
        );
        return true;
    }

    public Boolean substituteTask(String taskId, List<String> taskIds) {

        //storing student ids, that have completed the substituting task
        List<String> studentIds = studentTaskCompleteRepo.findStudentIdsByTaskId(taskId);

        //deleting records with the taskId that will be substituted
        studentTaskCompleteRepo.deleteAllByTaskId(taskId);

        //creating new records with the new tasks
        for (String studentId : studentIds) {
            for (String newTaskId : taskIds) {
                studentTaskCompleteRepo.save(
                        StudentTaskComplete.builder()
                                .studentId(studentId)
                                .taskId(newTaskId)
                                .completeDate(new Date(System.currentTimeMillis()))
                                .build()
                );
            }
        }

        return true;
    }

    public Boolean deleteTasks(List<String> taskIds) {
        studentTaskCompleteRepo.deleteAllByTaskIdIn(taskIds);
        return true;
    }

    public Boolean deleteTask(String taskId) {
        studentTaskCompleteRepo.deleteAllByTaskId(taskId);
        return true;
    }

    public Boolean addTask(String taskId, List<String> completedTaskIds) {

        //get student ids, that have completed any task from the list
        List<String> studentIds = studentTaskCompleteRepo.findStudentIdsByTaskIds(completedTaskIds);

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
}
