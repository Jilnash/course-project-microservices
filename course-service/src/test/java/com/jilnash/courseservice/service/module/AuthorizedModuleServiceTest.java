package com.jilnash.courseservice.service.module;

import com.jilnash.courseservice.dto.module.ModuleCreateDTO;
import com.jilnash.courseservice.dto.module.ModuleResponseDTO;
import com.jilnash.courseservice.dto.module.ModuleUpdateDTO;
import com.jilnash.courseservice.mapper.ModuleMapper;
import com.jilnash.courseservice.model.Module;
import com.jilnash.courseservice.service.courseauthr.CourseAuthorizationService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class AuthorizedModuleServiceTest {

    @Mock
    private CourseAuthorizationService courseAuthrService;

    @Mock
    private ModuleServiceImpl moduleService;

    @Mock
    private ModuleMapper moduleMapper;

    @InjectMocks
    private AuthorizedModuleService authorizedModuleService;

    public AuthorizedModuleServiceTest() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetModulesInCourse_withValidCourseId_returnsModuleList() {
        // Arrange
        String courseId = "course123";
        var mockModules = List.of(new Module(), new Module()); // Replace Object with actual entity class
        var mappedModules = List.of(
                ModuleResponseDTO.builder().build(),
                ModuleResponseDTO.builder().build()
        );

        when(moduleService.getModulesInCourse(courseId)).thenReturn(mockModules);
        when(moduleMapper.toResponse(any())).thenReturn(ModuleResponseDTO.builder().build());

        // Act
        List<ModuleResponseDTO> result = authorizedModuleService.getModulesInCourse(courseId);

        // Assert
        assertEquals(2, result.size());
        verify(moduleService, times(1)).getModulesInCourse(courseId);
        verify(moduleMapper, times(2)).toResponse(any());
    }


    @Test
    void testGetModuleByCourse_withValidIds_returnsModule() {
        // Arrange
        String courseId = "course123";
        String moduleId = "module456";
        var mockModule = new Module(); // Replace Object with actual entity class
        var mappedModule = ModuleResponseDTO.builder().build();

        when(moduleService.getModuleByCourse(courseId, moduleId)).thenReturn(mockModule);
        when(moduleMapper.toResponse(mockModule)).thenReturn(mappedModule);

        // Act
        ModuleResponseDTO result = authorizedModuleService.getModuleByCourse(courseId, moduleId);

        // Assert
        assertEquals(mappedModule, result);
        verify(moduleService, times(1)).getModuleByCourse(courseId, moduleId);
        verify(moduleMapper, times(1)).toResponse(mockModule);
    }


    @Test
    void testCreateModuleByUser_ValidTeacherAndModuleDetails_createsModule() {
        // Arrange
        String teacherId = "teacher123";
        ModuleCreateDTO moduleDTO = new ModuleCreateDTO();
        moduleDTO.setName("Module Name");
        moduleDTO.setDescription("Module Description");
        doNothing().when(courseAuthrService)
                .validateTeacherCourseRights(moduleDTO.getCourseId(), teacherId, List.of("CREATE"));
        when(moduleService.create(moduleDTO)).thenReturn(new Module());

        // Act
        Boolean result = authorizedModuleService.createModuleByUser(teacherId, moduleDTO);

        // Assert
        assertEquals(true, result);
        verify(courseAuthrService, times(1))
                .validateTeacherCourseRights(moduleDTO.getCourseId(), teacherId, List.of("CREATE"));
        verify(moduleService, times(1)).create(moduleDTO);
    }

    @Test
    void testCreateModuleByUser_InvalidTeacherRights_throwsException() {
        // Arrange
        String teacherId = "teacher123";
        ModuleCreateDTO moduleDTO = new ModuleCreateDTO();
        moduleDTO.setName("Module Name");
        moduleDTO.setDescription("Module Description");
        doThrow(new SecurityException("Unauthorized access"))
                .when(courseAuthrService)
                .validateTeacherCourseRights(eq(moduleDTO.getCourseId()), eq(teacherId), eq(List.of("CREATE")));

        // Act & Assert
        SecurityException exception = assertThrows(SecurityException.class, () -> {
            authorizedModuleService.createModuleByUser(teacherId, moduleDTO);
        });
        assertEquals("Unauthorized access", exception.getMessage());
        verify(courseAuthrService, times(1))
                .validateTeacherCourseRights(moduleDTO.getCourseId(), teacherId, List.of("CREATE"));
        verify(moduleService, times(0)).create(any(ModuleCreateDTO.class));
    }

    @Test
    void testUpdateModuleByUser_ValidTeacherAndModuleDetails_updatesModule() {
        // Arrange
        String teacherId = "teacher123";
        ModuleUpdateDTO moduleDTO = new ModuleUpdateDTO();
        moduleDTO.setName("Updated Name");
        moduleDTO.setDescription("Updated Description");
        doNothing().when(courseAuthrService)
                .validateTeacherCourseRights(moduleDTO.getCourseId(), teacherId, List.of("UPDATE"));
        when(moduleService.update(moduleDTO)).thenReturn(new Module());

        // Act
        Boolean result = authorizedModuleService.updateModuleByUser(teacherId, moduleDTO);

        // Assert
        assertEquals(true, result);
        verify(courseAuthrService, times(1))
                .validateTeacherCourseRights(moduleDTO.getCourseId(), teacherId, List.of("UPDATE"));
        verify(moduleService, times(1)).update(moduleDTO);
    }

    @Test
    void testUpdateModuleByUser_InvalidTeacherRights_throwsException() {
        // Arrange
        String teacherId = "teacher123";
        ModuleUpdateDTO moduleDTO = new ModuleUpdateDTO();
        moduleDTO.setName("Updated Name");
        moduleDTO.setDescription("Updated Description");
        doThrow(new SecurityException("Unauthorized access"))
                .when(courseAuthrService)
                .validateTeacherCourseRights(eq(moduleDTO.getCourseId()), eq(teacherId), eq(List.of("UPDATE")));

        // Act & Assert
        SecurityException exception = assertThrows(SecurityException.class, () -> {
            authorizedModuleService.updateModuleByUser(teacherId, moduleDTO);
        });
        assertEquals("Unauthorized access", exception.getMessage());
        verify(courseAuthrService, times(1))
                .validateTeacherCourseRights(moduleDTO.getCourseId(), teacherId, List.of("UPDATE"));
        verify(moduleService, times(0)).update(any(ModuleUpdateDTO.class));
    }
}