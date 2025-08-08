package com.jilnash.hwservicesaga.controller;

import com.jilnash.hwservicesaga.dto.HomeworkCreateSagaDTO;
import com.jilnash.hwservicesaga.service.HomeworkSagaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/courses/{courseId}/homeworks")
@RequiredArgsConstructor
public class HomeworkController {

    private final HomeworkSagaService homeworkService;

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

        return ResponseEntity.ok(homeworkService.getHomeworks(taskId, studentId, checked, createdAfter));
    }

    /**
     * Creates a new homework submission based on the provided data.
     * This includes setting the student ID from the request header and saving the homework entity.
     *
     * @param homeworkDTO a data transfer object containing the details of the homework to be created;
     *                    includes task ID, checked status, and associated files.
     * @param studentId   the unique identifier of the student, provided via the "X-User-Sub" request header.
     * @return a ResponseEntity containing an AppResponse object with the status,
     * message, and details of the created homework record.
     */
    @PostMapping
    public ResponseEntity<?> createHomework(
            @ModelAttribute @Validated HomeworkCreateSagaDTO homeworkDTO,
            @PathVariable String courseId,
            @RequestHeader("X-User-Sub") String studentId) {

        homeworkDTO.setStudentId(studentId);
        homeworkDTO.setCourseId(courseId);
        homeworkDTO.setHomeworkId(UUID.randomUUID().toString());

        log.info("[CONTROLLER] Creating homework");
        log.debug("[CONTROLLER] Creating homework to taskId {} by studentId: {}", homeworkDTO.getTaskId(), studentId);

        homeworkService.createHomework(homeworkDTO);
        return ResponseEntity.ok("Homework created successfully");
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

        return ResponseEntity.ok(homeworkService.getHomework(id));
    }


    /**
     * Marks the homework associated with the given ID as checked.
     *
     * @param hwId the unique identifier of the homework to be marked as checked
     * @return a ResponseEntity containing an AppResponse object with the status,
     * message, and details of the updated homework
     */
    @PatchMapping("{hwId}/checked")
    public ResponseEntity<?> setChecked(@PathVariable UUID hwId,
                                        @PathVariable String courseId,
                                        @RequestHeader("X-User-Sub") String teacherId) {

        log.info("[CONTROLLER] Checking homework");
        log.debug("[CONTROLLER] Checking homework with id: {}", hwId);
        homeworkService.setHomeworkChecked(courseId, teacherId, hwId);

        return ResponseEntity.ok("Homework checked successfully");
    }

    /**
     * Soft deletes the homework associated with the given ID.
     * This method performs a soft delete, marking the homework as deleted without removing it from the database.
     *
     * @param id the unique identifier of the homework to be deleted
     * @return a ResponseEntity containing an AppResponse object with the status and message of the deletion operation
     */
    @DeleteMapping("/{id}/soft")
    public ResponseEntity<?> deleteHomework(@PathVariable UUID id,
                                            @PathVariable String courseId,
                                            @RequestHeader("X-User-Sub") String teacherId) {

        log.info("[CONTROLLER] Deleting homework");
        log.debug("[CONTROLLER] Deleting homework with id: {}", id);
        homeworkService.softDeleteHomework(courseId, teacherId, id);

        return ResponseEntity.ok("Homework deleted successfully");
    }

    /**
     * Hard deletes the homework associated with the given ID.
     * This method performs a hard delete, removing the homework from the database permanently.
     *
     * @param id the unique identifier of the homework to be deleted
     * @return a ResponseEntity containing an AppResponse object with the status and message of the deletion operation
     */
    @DeleteMapping("/{id}/hard")
    public ResponseEntity<?> hardDeleteHomework(@PathVariable UUID id,
                                                @PathVariable String courseId,
                                                @RequestHeader("X-User-Sub") String teacherId) {

        log.info("[CONTROLLER] Hard deleting homework");
        log.debug("[CONTROLLER] Hard deleting homework with id: {}", id);
        homeworkService.hardDeleteHomework(courseId, teacherId, id);

        return ResponseEntity.ok("Homework hard deleted successfully");
    }
}
