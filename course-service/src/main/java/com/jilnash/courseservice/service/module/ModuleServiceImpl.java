package com.jilnash.courseservice.service.module;

import com.jilnash.courseservice.dto.module.ModuleCreateDTO;
import com.jilnash.courseservice.dto.module.ModuleUpdateDTO;
import com.jilnash.courseservice.mapper.ModuleMapper;
import com.jilnash.courseservice.model.Module;
import com.jilnash.courseservice.repo.ModuleRepo;
import com.jilnash.courseservice.service.course.CourseService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModuleServiceImpl implements ModuleService {

    private final ModuleRepo moduleRepo;

    private final CourseService courseService;

    private final ModuleMapper moduleMapper;

    public ModuleServiceImpl(ModuleRepo moduleRepo, CourseService courseService, ModuleMapper moduleMapper) {
        this.moduleRepo = moduleRepo;
        this.courseService = courseService;
        this.moduleMapper = moduleMapper;
    }

    @Override
    public List<Module> getModules(String id, String name) {

        if (!name.isEmpty())
            return moduleRepo.findAllByNameContainingAndCourseId(name, id);

        return moduleRepo.findAllByCourseId(id);
    }

    @Override
    public Module getModule(String id) {
        return moduleRepo
                .findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Module not found with id: " + id));
    }

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

        return moduleRepo.save(moduleMapper.toNode(moduleDTO));
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
}
