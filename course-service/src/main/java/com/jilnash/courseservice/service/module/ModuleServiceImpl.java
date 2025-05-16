package com.jilnash.courseservice.service.module;

import com.jilnash.courseservice.dto.module.ModuleCreateDTO;
import com.jilnash.courseservice.dto.module.ModuleUpdateDTO;
import com.jilnash.courseservice.mapper.ModuleMapper;
import com.jilnash.courseservice.model.Module;
import com.jilnash.courseservice.repo.ModuleRepo;
import com.jilnash.courseservice.service.course.CourseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * This service implementation provides operations to manage modules within courses.
 * It interacts with the ModuleRepo for data persistence and operations, and uses
 * CourseServiceImpl for course-related validations. ModuleMapper is used for transforming
 * data objects between different layers.
 */
@Service
@RequiredArgsConstructor
public class ModuleServiceImpl implements ModuleService {

    private final ModuleRepo moduleRepo;

    private final CourseServiceImpl courseService;

    private final ModuleMapper moduleMapper;

    /**
     * Retrieves all modules associated with a specific course.
     *
     * @param courseId the unique identifier of the course for which the modules are to be retrieved
     * @return a list of modules that belong to the specified course
     */
    @Override
    public List<Module> getModulesInCourse(String courseId) {

        return moduleRepo.findAllByCourseId(courseId);
    }

    /**
     * Retrieves a specific module by its unique identifier and the course it belongs to.
     *
     * @param courseId the unique identifier of the course that the module belongs to
     * @param id       the unique identifier of the module to be retrieved
     * @return the module corresponding to the given identifiers
     * @throws UsernameNotFoundException if no module is found with the specified identifiers
     */
    @Override
    @Cacheable(value = "modules", key = "#id")
    public Module getModuleByCourse(String courseId, String id) {
        return moduleRepo
                .findByIdAndCourseId(id, courseId)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Module not found with id: " + id + " in course: " + courseId
                ));
    }

    /**
     * Creates a new module based on the provided ModuleCreateDTO.
     * The method ensures the course associated with the module exists
     * before mapping and saving the module entity in the repository.
     *
     * @param moduleDTO the data transfer object containing details for creating a module
     *                  including its name, description, and associated course information
     * @return the newly created Module entity saved in the repository
     */
    @Override
    public Module create(ModuleCreateDTO moduleDTO) {

        // check if course exists then set it to moduleDTO
        moduleDTO.setCourse(courseService.getCourse(moduleDTO.getCourseId()));

        return moduleRepo.save(moduleMapper.toNode(moduleDTO));
    }

    /**
     * Updates an existing module with the provided details from the ModuleUpdateDTO.
     * The method validates if the module exists for the given id and courseId before updating.
     *
     * @param moduleDTO the data transfer object containing updated details of the module,
     *                  including its id, name, description, and the course it belongs to
     * @return the updated Module entity with the changes applied
     * @throws UsernameNotFoundException if the module does not exist with the specified id and courseId
     */
    @Override
    public Module update(ModuleUpdateDTO moduleDTO) {

        // check if module exists by id and courseId
        if (!moduleRepo.existsByIdAndCourseId(moduleDTO.getId(), moduleDTO.getCourseId()))
            throw new UsernameNotFoundException("Module not found with id: " + moduleDTO.getId());

        return moduleRepo.updateModuleData(
                moduleDTO.getId(),
                moduleDTO.getName(),
                moduleDTO.getDescription()
        );
    }

    /**
     * Deletes the module identified by the given unique identifier.
     *
     * @param id the unique identifier of the module to be deleted
     * @return the deleted Module entity, or null if the module was not found
     */
    @Override
    public Module delete(String id) {
        return null;
    }

    /**
     * Determines whether the module with the given identifier has at least one associated task.
     *
     * @param id the unique identifier of the module
     * @return true if the module contains at least one task, false otherwise
     * @throws UsernameNotFoundException if the module with the specified identifier is not found
     */
    public Boolean hasAtLeastOneTask(String id) {
        return moduleRepo
                .hasAtLeastOneTask(id)
                .orElseThrow(() -> new UsernameNotFoundException("Module does not exist"));
    }

    /**
     * Validates if a module exists within a specific course.
     * This method checks whether a module with the given identifier is associated with the specified course.
     * If the module does not exist in the course, an exception is thrown.
     *
     * @param moduleId the unique identifier of the module to validate
     * @param courseId the unique identifier of the course in which the module should exist
     * @throws RuntimeException if the module is not found within the specified course
     */
    public void validateModuleExistsInCourse(String moduleId, String courseId) {
        if (!moduleRepo.existsByIdAndCourseId(moduleId, courseId))
            throw new RuntimeException("Module not found with id: " + moduleId + " in course: " + courseId);
    }

    /**
     * Validates that a module contains all the specified tasks.
     * If the module does not include all tasks listed in the given set, an exception is thrown.
     *
     * @param moduleId the unique identifier of the module to validate
     * @param taskIds a set of task identifiers to check for inclusion in the module
     * @throws RuntimeException if the module does not contain all specified tasks
     */
    public void validateModuleContainsAllTasks(String moduleId, Set<String> taskIds) {
        if (!moduleRepo.containsTasks(moduleId, taskIds))
            throw new RuntimeException("Module does not contain all tasks in: " + taskIds);
    }
}
