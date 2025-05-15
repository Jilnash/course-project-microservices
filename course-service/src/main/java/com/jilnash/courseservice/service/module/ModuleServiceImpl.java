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

@Service
@RequiredArgsConstructor
public class ModuleServiceImpl implements ModuleService {

    private final ModuleRepo moduleRepo;

    private final CourseServiceImpl courseService;

    @Override
    public List<Module> getModulesInCourse(String courseId) {

        return moduleRepo.findAllByCourseId(courseId);
    }

    @Override
    @Cacheable(value = "modules", key = "#id")
    public Module getModuleByCourse(String courseId, String id) {
        return moduleRepo
                .findByIdAndCourseId(id, courseId)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Module not found with id: " + id + " in course: " + courseId
                ));
    }

    @Override
    public Module create(ModuleCreateDTO moduleDTO) {

        // check if course exists then set it to moduleDTO
        moduleDTO.setCourse(courseService.getCourse(moduleDTO.getCourseId()));

        return moduleRepo.save(ModuleMapper.toNode(moduleDTO));
    }

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

    @Override
    public Module delete(String id) {
        return null;
    }

    public Boolean hasAtLeastOneTask(String id) {
        return moduleRepo
                .hasAtLeastOneTask(id)
                .orElseThrow(() -> new UsernameNotFoundException("Module does not exist"));
    }

    public void validateModuleExistsInCourse(String moduleId, String courseId) {
        if (!moduleRepo.existsByIdAndCourseId(moduleId, courseId))
            throw new RuntimeException("Module not found with id: " + moduleId + " in course: " + courseId);
    }

    public void validateModuleContainsAllTasks(String moduleId, Set<String> taskIds) {
        if (!moduleRepo.containsTasks(moduleId, taskIds))
            throw new RuntimeException("Module does not contain all tasks in: " + taskIds);
    }
}
