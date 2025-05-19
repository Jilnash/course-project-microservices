package com.jilnash.courseservice.service.module;

import com.jilnash.courseservice.dto.module.ModuleCreateDTO;
import com.jilnash.courseservice.dto.module.ModuleResponseDTO;
import com.jilnash.courseservice.dto.module.ModuleUpdateDTO;
import com.jilnash.courseservice.mapper.ModuleMapper;
import com.jilnash.courseservice.service.courseauthr.CourseAuthorizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The AuthorizedModuleService class provides services to manage and interact with modules
 * associated with a course. It acts as a bridge between course authorization, module services,
 * and data mapping layers. It ensures that only users with permissions can create, update, or retrieve
 * modules within a specific course.
 * <p>
 * The service methods include:
 * - Fetching all modules associated with a given course.
 * - Retrieving specific module details by course and module IDs.
 * - Creating a new module for a course by an authorized teacher.
 * - Updating module details for a course by an authorized teacher.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorizedModuleService {

    private final CourseAuthorizationService courseAuthrService;

    private final ModuleServiceImpl moduleService;

    private final ModuleMapper moduleMapper;

    /**
     * Retrieves a list of modules associated with a specific course.
     *
     * @param courseId The unique identifier of the course for which the modules are being fetched.
     * @return A list of ModuleResponseDTO objects representing the details of the modules within the course.
     */
    public List<ModuleResponseDTO> getModulesInCourse(String courseId) {

        log.info("[SERVICE] Fetching modules in course: {}", courseId);

        return moduleService.getModulesInCourse(courseId).stream().map(moduleMapper::toResponse).toList();
    }

    /**
     * Retrieves the details of a specific module associated with a given course.
     *
     * @param courseId The unique identifier of the course from which the module is being fetched.
     * @param moduleId The unique identifier of the module being fetched within the course.
     * @return A ModuleResponseDTO object representing the details of the requested module.
     */
    public ModuleResponseDTO getModuleByCourse(String courseId, String moduleId) {

        log.info("[SERVICE] Fetching module {} in course: {}", moduleId, courseId);

        return moduleMapper.toResponse(moduleService.getModuleByCourse(courseId, moduleId));
    }

    /**
     * Creates a new module for a course based on the provided details and ensures that the
     * teacher has the necessary rights to perform the operation.
     *
     * @param teacherId The unique identifier of the teacher attempting to create the module.
     * @param moduleDTO An object containing the details of the module to be created, including
     *                  its name, description, and associated course ID.
     * @return A Boolean value indicating whether the module was successfully created.
     */
    public Boolean createModuleByUser(String teacherId, ModuleCreateDTO moduleDTO) {
        courseAuthrService.validateTeacherCourseRights(moduleDTO.getCourseId(), teacherId, List.of("CREATE"));
        log.info("[SERVICE] Creating module in course: {}", moduleDTO.getCourseId());
        moduleService.create(moduleDTO);
        return true;
    }

    /**
     * Updates an existing module within a course based on the provided details and ensures
     * that the teacher has the necessary rights to perform this operation.
     *
     * @param teacherId The unique identifier of the teacher attempting to update the module.
     * @param moduleDTO An object containing the updated details of the module, including its ID,
     *                  name, description, and associated course ID.
     * @return A Boolean value indicating whether the module was successfully updated.
     */
    public Boolean updateModuleByUser(String teacherId, ModuleUpdateDTO moduleDTO) {
        courseAuthrService.validateTeacherCourseRights(moduleDTO.getCourseId(), teacherId, List.of("UPDATE"));
        log.info("[SERVICE] Updating module in course: {}", moduleDTO.getCourseId());
        moduleService.update(moduleDTO);
        return true;
    }
}
