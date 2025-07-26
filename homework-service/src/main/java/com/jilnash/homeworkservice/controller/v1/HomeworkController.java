package com.jilnash.homeworkservice.controller.v1;

import com.jilnash.homeworkservice.dto.AppResponse;
import com.jilnash.homeworkservice.mapper.HomeworkMapper;
import com.jilnash.homeworkservice.service.HomeworkServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

/**
 * Controller for managing homework-related operations in the application.
 * Handles requests and responses for fetching, creating, and updating homework entities.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/homeworks")
@RequiredArgsConstructor
public class HomeworkController {

    private final HomeworkServiceImpl homeworkService;

    private final HomeworkMapper homeworkMapper;

    /**
     * Retrieves a list of homeworks based on the provided filtering criteria.
     *
     * @param taskId       the ID of the task to filter homeworks by; can be null to retrieve homeworks without this filter.
     * @param studentId    the ID of the student to filter homeworks by; can be null to retrieve homeworks without this filter.
     * @param checked      a flag indicating whether to filter homeworks based on their checked status; can be null to retrieve homeworks without this filter.
     * @param createdAfter the date to filter homeworks created on or after this date; can be null to retrieve homeworks without this filter.
     * @return a ResponseEntity containing a response object with the filtered list of homeworks and the response metadata.
     */
    @GetMapping
    public ResponseEntity<?> getHomeworks(
            @RequestParam(required = false) String taskId,
            @RequestParam(required = false) String studentId,
            @RequestParam(required = false) Boolean checked,
            @RequestParam(required = false) Date createdAfter
    ) {

        log.info("[CONTROLLER] Fetching homeworks");
        log.debug("[CONTROLLER] Fetching homeworks with taskId: {}, studentId: {}, checked: {}, createdAfter: {}",
                taskId, studentId, checked, createdAfter);

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Homeworks fetched successfully",
                        homeworkService.getHomeworks(taskId, studentId, checked, createdAfter)
                )
        );
    }

    /**
     * Retrieves a specific homework entity based on the provided unique identifier.
     *
     * @param id the unique identifier of the homework to retrieve
     * @return a ResponseEntity containing an AppResponse object with the status,
     * message, and details of the fetched homework
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getHomework(@PathVariable UUID id) {

        log.info("[CONTROLLER] Fetching homework");
        log.debug("[CONTROLLER] Fetching homework with id: {}", id);

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Homework fetched successfully",
                        homeworkMapper.toResponseDTO(homeworkService.getHomework(id))
                )
        );
    }

    /**
     * Retrieves the task ID associated with a given homework ID.
     *
     * @param id the unique identifier of the homework for which the task ID is to be fetched
     * @return the task ID associated with the specified homework ID
     */
    @GetMapping("{id}/task-id")
    public String getHomeworkTaskId(@PathVariable UUID id) {

        log.info("[CONTROLLER] Fetching task id");
        log.debug("[CONTROLLER] Fetching task id with homework id: {}", id);

        return homeworkService.getHomeworkTaskId(id);
    }

    /**
     * Retrieves the student ID associated with a given homework ID.
     *
     * @param hwId the unique identifier of the homework for which the student ID is to be fetched
     * @return the student ID associated with the specified homework ID
     */
    @GetMapping("{hwId}/student-id")
    public String getHomeworkStudentId(@PathVariable UUID hwId) {

        log.info("[CONTROLLER] Fetching student id");
        log.debug("[CONTROLLER] Fetching student id with homework id: {}", hwId);

        return homeworkService.getHomeworkStudentId(hwId);
    }

    /**
     * Checks if the homework associated with the given ID has been checked.
     *
     * @param hwId the unique identifier of the homework to check
     * @return a ResponseEntity containing an AppResponse object with the status,
     * message, and checked status of the homework
     */
    @GetMapping("{hwId}/checked")
    public Boolean isChecked(@PathVariable UUID hwId) {

        log.info("[CONTROLLER] Checking homework status");
        log.debug("[CONTROLLER] Checking homework status with id: {}", hwId);

        return homeworkService.isHomeworkChecked(hwId);
    }

    /**
     * Retrieves the attempt number of the homework associated with the given ID.
     *
     * @param hwId the unique identifier of the homework for which the attempt number is to be fetched
     * @return the attempt number of the specified homework
     */
    @GetMapping("{hwId}/attempt")
    public Integer getHomeworkAttempt(@PathVariable UUID hwId) {

        log.info("[CONTROLLER] Fetching homework attempt");
        log.debug("[CONTROLLER] Fetching homework attempt with id: {}", hwId);

        return homeworkService.getHomeworkAttempt(hwId);
    }

    /**
     * Retrieves the creation date of the homework associated with the given ID.
     *
     * @param hwId the unique identifier of the homework for which the creation date is to be fetched
     * @return the creation date of the specified homework
     */
    @GetMapping("{hwId}/created-at")
    public Date getHomeworkCreatedAt(@PathVariable UUID hwId) {

        log.info("[CONTROLLER] Fetching homework creation date");
        log.debug("[CONTROLLER] Fetching homework creation date with id: {}", hwId);

        return homeworkService.getHomeworkCreatedAt(hwId);
    }

    /**
     * Retrieves the creation date of the homework associated with the given ID.
     *
     * @param hwId the unique identifier of the homework for which the creation date is to be fetched
     * @return the creation date of the specified homework
     */
    @GetMapping("{hwId}/filenames")
    public List<String> getHomeworkFileNames(@PathVariable UUID hwId) {

        log.info("[CONTROLLER] Fetching homework file names");
        log.debug("[CONTROLLER] Fetching homework file names with id: {}", hwId);

        return homeworkService.getHomeworkFileNames(hwId);
    }
}

