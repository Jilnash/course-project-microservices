package com.jilnash.courseservice.controller.v1;

import com.jilnash.courseservice.dto.task.TaskLinkDTO;
import com.jilnash.courseservice.service.taskconnection.TaskConnectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/api/v1/courses/{courseId}/modules/{moduleId}/tasks/connections")
@RequiredArgsConstructor
public class TaskConnectionController {

    private final TaskConnectionService taskConnectionService;

    @PostMapping
    public ResponseEntity<?> createTaskConnections(@RequestBody Set<TaskLinkDTO> connections) {
        log.info("Creating task connections: {}", connections);

        taskConnectionService.createTaskConnections(connections);
        return ResponseEntity.ok("Connections created succesfully");
    }

    @PutMapping
    public ResponseEntity<?> changeTaskConnection(@RequestBody Map<String, TaskLinkDTO> map) {
        log.info("[CONTROLLER] Changing task connection from {} to {}", map.get("before"), map.get("after"));

        taskConnectionService.changeTaskConnection(map.get("before"), map.get("after"));
        return ResponseEntity.ok("Connection changed successfully");
    }

    @DeleteMapping("/soft")
    public ResponseEntity<?> softDeleteTaskConnections(@RequestBody Set<TaskLinkDTO> connections) {
        log.info("Soft deleting task connections: {}", connections);

        taskConnectionService.softDeleteTaskConnections(connections);
        return ResponseEntity.ok("Connections soft deleted successfully");
    }

    @DeleteMapping("/hard")
    public ResponseEntity<?> hardDeleteTaskConnections(@RequestBody Set<TaskLinkDTO> connections) {
        log.info("Deleting task connections: {}", connections);

        taskConnectionService.hardDeleteTaskConnections(connections);
        return ResponseEntity.ok("Connections deleted successfully");
    }
}
