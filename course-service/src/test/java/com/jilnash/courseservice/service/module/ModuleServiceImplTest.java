package com.jilnash.courseservice.service.module;

import com.jilnash.courseservice.dto.module.ModuleCreateDTO;
import com.jilnash.courseservice.dto.module.ModuleUpdateDTO;
import com.jilnash.courseservice.mapper.ModuleMapper;
import com.jilnash.courseservice.model.Course;
import com.jilnash.courseservice.model.Module;
import com.jilnash.courseservice.repo.ModuleRepo;
import com.jilnash.courseservice.service.course.CourseServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class ModuleServiceImplTest {

    @Mock
    private ModuleRepo moduleRepo;

    @Mock
    private CourseServiceImpl courseService;

    @Mock
    private ModuleMapper moduleMapper;

    @InjectMocks
    private ModuleServiceImpl moduleServiceImpl;

    public ModuleServiceImplTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetModuleByCourse_SuccessfulRetrieval() {
        String courseId = "course-123";
        String moduleId = "module-123";

        Module mockModule = Module.builder()
                .id(moduleId)
                .name("Sample Module")
                .description("Sample Description")
                .build();

        when(moduleRepo.findByIdAndCourseId(moduleId, courseId)).thenReturn(Optional.of(mockModule));

        Module result = moduleServiceImpl.getModuleByCourse(courseId, moduleId);

        assertNotNull(result);
        assertEquals(moduleId, result.getId());
        assertEquals("Sample Module", result.getName());
        assertEquals("Sample Description", result.getDescription());
    }

    @Test
    void testGetModuleByCourse_HasNoMatchingModule() {
        String courseId = "course-123";
        String moduleId = "non-existent-module";

        when(moduleRepo.findByIdAndCourseId(moduleId, courseId)).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> moduleServiceImpl.getModuleByCourse(courseId, moduleId));

        assertEquals("Module not found with id: " + moduleId + " in course: " + courseId, exception.getMessage());
    }


    @Test
    void testGetModulesInCourse_SuccessfulRetrieval() {
        String courseId = "course-123";

        Module mockModule1 = Module.builder()
                .id("module-1")
                .name("Module 1")
                .description("Description 1")
                .build();

        Module mockModule2 = Module.builder()
                .id("module-2")
                .name("Module 2")
                .description("Description 2")
                .build();

        when(moduleRepo.findAllByCourseId(courseId)).thenReturn(List.of(mockModule1, mockModule2));

        List<Module> result = moduleServiceImpl.getModulesInCourse(courseId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("module-1", result.get(0).getId());
        assertEquals("Module 1", result.get(0).getName());
        assertEquals("Description 1", result.get(0).getDescription());
        assertEquals("module-2", result.get(1).getId());
        assertEquals("Module 2", result.get(1).getName());
        assertEquals("Description 2", result.get(1).getDescription());
    }

    @Test
    void testGetModulesInCourse_NoModulesFound() {
        String courseId = "course-123";

        when(moduleRepo.findAllByCourseId(courseId)).thenReturn(List.of());

        List<Module> result = moduleServiceImpl.getModulesInCourse(courseId);

        assertNotNull(result);

    }

    @Test
    void testCreateModule_SuccessfulCreation() {
        ModuleCreateDTO moduleCreateDTO = new ModuleCreateDTO();
        moduleCreateDTO.setName("New Module");
        moduleCreateDTO.setDescription("A new module description");
        String courseId = "course-123";
        moduleCreateDTO.setCourseId(courseId);

        Module mockModule = Module.builder()
                .id("module-123")
                .name("New Module")
                .description("A new module description")
                .build();

        when(courseService.getCourse(courseId)).thenReturn(new Course());
        when(moduleMapper.toNode(moduleCreateDTO)).thenReturn(mockModule);
        when(moduleRepo.save(mockModule)).thenReturn(mockModule);

        Module result = moduleServiceImpl.create(moduleCreateDTO);

        assertNotNull(result);
        assertEquals("module-123", result.getId());
        assertEquals("New Module", result.getName());
        assertEquals("A new module description", result.getDescription());
    }

    @Test
    void testCreateModule_CourseNotFound() {
        ModuleCreateDTO moduleCreateDTO = new ModuleCreateDTO();
        moduleCreateDTO.setName("New Module");
        moduleCreateDTO.setDescription("A new module description");
        String courseId = "invalid-course";
        moduleCreateDTO.setCourseId(courseId);

        when(courseService.getCourse(courseId)).thenThrow(new UsernameNotFoundException("Course not found with id: " + courseId));

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> moduleServiceImpl.create(moduleCreateDTO));

        assertEquals("Course not found with id: " + courseId, exception.getMessage());
    }


    @Test
    void testUpdateModule_SuccessfulUpdate() {
        ModuleUpdateDTO moduleUpdateDTO = new ModuleUpdateDTO();
        moduleUpdateDTO.setId("module-123");
        moduleUpdateDTO.setCourseId("course-123");
        moduleUpdateDTO.setName("Updated Module");
        moduleUpdateDTO.setDescription("Updated Description");

        Module updatedModule = Module.builder()
                .id("module-123")
                .name("Updated Module")
                .description("Updated Description")
                .build();

        when(moduleRepo.existsByIdAndCourseId(moduleUpdateDTO.getId(), moduleUpdateDTO.getCourseId())).thenReturn(true);
        when(moduleRepo.updateModuleData(moduleUpdateDTO.getId(), moduleUpdateDTO.getName(), moduleUpdateDTO.getDescription()))
                .thenReturn(updatedModule);

        Module result = moduleServiceImpl.update(moduleUpdateDTO);

        assertNotNull(result);
        assertEquals("module-123", result.getId());
        assertEquals("Updated Module", result.getName());
        assertEquals("Updated Description", result.getDescription());
    }

    @Test
    void testUpdateModule_ModuleNotFound() {
        ModuleUpdateDTO moduleUpdateDTO = new ModuleUpdateDTO();
        moduleUpdateDTO.setId("non-existent-module");
        moduleUpdateDTO.setCourseId("course-123");
        moduleUpdateDTO.setName("Updated Module");
        moduleUpdateDTO.setDescription("Updated Description");

        when(moduleRepo.existsByIdAndCourseId(moduleUpdateDTO.getId(), moduleUpdateDTO.getCourseId())).thenReturn(false);

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> moduleServiceImpl.update(moduleUpdateDTO));

        assertEquals("Module not found with id: non-existent-module", exception.getMessage());
    }

    @Test
    void testHasAtLeastOneTask_ReturnsTrue() {
        String moduleId = "module-123";

        when(moduleRepo.hasAtLeastOneTask(moduleId)).thenReturn(Optional.of(true));

        Boolean result = moduleServiceImpl.hasAtLeastOneTask(moduleId);

        assertNotNull(result);
        assertTrue(result);
    }

    @Test
    void testHasAtLeastOneTask_ThrowsModuleNotFoundException() {
        String moduleId = "non-existent-module";

        when(moduleRepo.hasAtLeastOneTask(moduleId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> moduleServiceImpl.hasAtLeastOneTask(moduleId));

        assertEquals("Module does not exist", exception.getMessage());
    }

    @Test
    void testHasAtLeastOneTask_ReturnsFalse() {
        String moduleId = "module-without-tasks";

        when(moduleRepo.hasAtLeastOneTask(moduleId)).thenReturn(Optional.of(false));

        Boolean result = moduleServiceImpl.hasAtLeastOneTask(moduleId);

        assertNotNull(result);
        assertFalse(result);
    }

    @Test
    void testValidateModuleExistsInCourse_ModuleExists() {
        String moduleId = "module-456";
        String courseId = "course-789";

        when(moduleRepo.existsByIdAndCourseId(moduleId, courseId)).thenReturn(true);

        assertDoesNotThrow(() -> moduleServiceImpl.validateModuleExistsInCourse(moduleId, courseId));
    }

    @Test
    void testValidateModuleExistsInCourse_ModuleDoesNotExist() {
        String moduleId = "non-existent-module";
        String courseId = "course-789";

        when(moduleRepo.existsByIdAndCourseId(moduleId, courseId)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> moduleServiceImpl.validateModuleExistsInCourse(moduleId, courseId));

        assertEquals("Module not found with id: " + moduleId + " in course: " + courseId, exception.getMessage());
    }

    @Test
    void testValidateModuleContainsAllTasks_AllTasksExist() {
        String moduleId = "module-123";
        Set<String> taskIds = Set.of("task-1", "task-2", "task-3");

        when(moduleRepo.containsTasks(moduleId, taskIds)).thenReturn(true);

        assertDoesNotThrow(() -> moduleServiceImpl.validateModuleContainsAllTasks(moduleId, taskIds));
    }

    @Test
    void testValidateModuleContainsAllTasks_NotAllTasksExist() {
        String moduleId = "module-123";
        Set<String> taskIds = Set.of("task-1", "task-4");

        when(moduleRepo.containsTasks(moduleId, taskIds)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> moduleServiceImpl.validateModuleContainsAllTasks(moduleId, taskIds));

        assertEquals("Module does not contain all tasks in: " + taskIds, exception.getMessage());
    }
}