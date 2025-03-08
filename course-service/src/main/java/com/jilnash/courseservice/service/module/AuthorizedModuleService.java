package com.jilnash.courseservice.service.module;

import com.jilnash.courseservice.dto.module.ModuleCreateDTO;
import com.jilnash.courseservice.dto.module.ModuleResponseDTO;
import com.jilnash.courseservice.dto.module.ModuleUpdateDTO;
import com.jilnash.courseservice.mapper.ModuleMapper;
import com.jilnash.courseservice.service.courseauthr.CourseAuthorizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorizedModuleService {

    private final CourseAuthorizationService courseAuthrService;

    private final ModuleServiceImpl moduleService;

    public List<ModuleResponseDTO> getModulesInCourse(String courseId) {

        return moduleService.getModulesInCourse(courseId).stream().map(ModuleMapper::toResponse).toList();
    }

    public ModuleResponseDTO getModuleByCourse(String courseId, String moduleId) {
        return ModuleMapper.toResponse(moduleService.getModuleByCourse(courseId, moduleId));
    }

    public Boolean createModuleByUser(String teacherId, ModuleCreateDTO moduleDTO) {
        courseAuthrService.validateTeacherCourseRights(moduleDTO.getCourseId(), teacherId, List.of("CREATE"));
        moduleService.create(moduleDTO);
        return true;
    }

    public Boolean updateModuleByUser(String teacherId, ModuleUpdateDTO moduleDTO) {
        courseAuthrService.validateTeacherCourseRights(moduleDTO.getCourseId(), teacherId, List.of("UPDATE"));
        moduleService.update(moduleDTO);
        return true;
    }
}
