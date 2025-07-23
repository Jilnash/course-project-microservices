package com.jilnash.courseservice.service.module;

import com.jilnash.courseservice.mapper.ModuleMapper;
import com.jilnash.courseservice.model.Module;
import com.jilnash.courseservice.repo.ModuleRepo;
import com.jilnash.courseservice.service.course.CourseServiceImpl;
import com.jilnash.courseservicedto.dto.module.ModuleCreateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
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
    public List<Module> getModules(String courseId) {

        return moduleRepo.findAllByCourseId(courseId);
    }

    @Override
    public Module getModule(String id) {
        return moduleRepo.findModuleById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Module not found with id: " + id));
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
    public Module getModule(String courseId, String id) {
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
    public Module createModule(ModuleCreateDTO moduleDTO) {

        // check if course exists by calling `getCourse` then set it to moduleDTO
//        moduleDTO.setCourse(courseService.getCourse(moduleDTO.getCourseId()));

        Module newModule = moduleMapper.toNode(moduleDTO);
        newModule.setCourse(courseService.getCourse(moduleDTO.getCourseId()));

        return moduleRepo.save(newModule);
    }

    @Override
    public Boolean updateModuleName(String courseId, String id, String name) {

        // check if module exists by id and courseId
        if (!moduleRepo.existsByIdAndCourseId(id, courseId))
            throw new NoSuchElementException("Module not found with id: " + id + " in course: " + courseId);

        moduleRepo.updateModuleName(id, name);
        return true;
    }

    @Override
    public Boolean updateModuleDescription(String courseId, String id, String description) {

        // check if module exists by id and courseId
        if (!moduleRepo.existsByIdAndCourseId(id, courseId))
            throw new NoSuchElementException("Module not found with id: " + id + " in course: " + courseId);

        moduleRepo.updateModuleDescription(id, description);
        return true;
    }

    /**
     * Deletes the module identified by the given unique identifier.
     *
     * @param id the unique identifier of the module to be deleted
     * @return the deleted Module entity, or null if the module was not found
     */
    @Override
    public Boolean softDeleteModule(String id) {
        return moduleRepo.findModuleById(id)
                .map(module -> {
                    module.setDeletedAt(new java.util.Date());
                    moduleRepo.save(module);
                    return true;
                })
                .orElseThrow(() -> new NoSuchElementException("Module not found with id: " + id));
    }

    @Override
    public Boolean hardDeleteModule(String id) {
        return null;
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
    @Override
    public void validateModuleExists(String moduleId, String courseId) {
        if (!moduleRepo.existsByIdAndCourseId(moduleId, courseId))
            throw new RuntimeException("Module not found with id: " + moduleId + " in course: " + courseId);
    }

    /**
     * Validates that a module contains all the specified tasks.
     * If the module does not include all tasks listed in the given set, an exception is thrown.
     *
     * @param moduleId the unique identifier of the module to validate
     * @param taskIds  a set of task identifiers to check for inclusion in the module
     * @throws RuntimeException if the module does not contain all specified tasks
     */
    @Override
    public void validateModuleContainsAllTasks(String moduleId, Set<String> taskIds) {
        if (!moduleRepo.containsTasks(moduleId, taskIds))
            throw new RuntimeException("Module does not contain all tasks in: " + taskIds);
    }

    /**
     * Determines whether the module with the given identifier has at least one associated task.
     *
     * @param id the unique identifier of the module
     * @return true if the module contains at least one task, false otherwise
     * @throws UsernameNotFoundException if the module with the specified identifier is not found
     */
    @Override
    public Boolean hasAtLeastOneTask(String id) {
        return moduleRepo
                .hasAtLeastOneTask(id)
                .orElseThrow(() -> new RuntimeException("Module does not exist"));
    }
}
