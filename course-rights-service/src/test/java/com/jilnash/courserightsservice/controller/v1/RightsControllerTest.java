package com.jilnash.courserightsservice.controller.v1;

import com.jilnash.courserightsservice.service.TeacherRightsService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RightsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeacherRightsService teacherRightsService;

    @Test
    void testGetRights_withValidTeacherAndCourse_shouldReturnRights() throws Exception {
        // Arrange
        String teacherId = "teacher123";
        String courseId = "course456";
        Set<String> mockRights = Set.of("VIEW", "EDIT");
        Mockito.when(teacherRightsService.getRights(courseId, teacherId)).thenReturn(mockRights);

        // Act & Assert
        mockMvc.perform(get("/api/v1/courses/{courseId}/teachers/{teacherId}/rights", courseId, teacherId))
                .andExpect(status().isOk())
                .andExpect(content().json("[\"VIEW\",\"EDIT\"]"));
    }

    @Test
    void testGetRights_withNoRights_shouldReturnEmptySet() throws Exception {
        // Arrange
        String teacherId = "teacher789";
        String courseId = "course012";
        Mockito.when(teacherRightsService.getRights(courseId, teacherId)).thenReturn(Set.of());

        // Act & Assert
        mockMvc.perform(get("/api/v1/courses/{courseId}/teachers/{teacherId}/rights", courseId, teacherId))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void testGetRights_withInvalidTeacherOrCourse_shouldReturnEmptySet() throws Exception {
        // Arrange
        String teacherId = "nonexistentTeacher";
        String courseId = "nonexistentCourse";
        Mockito.when(teacherRightsService.getRights(courseId, teacherId)).thenReturn(Set.of());

        // Act & Assert
        mockMvc.perform(get("/api/v1/courses/{courseId}/teachers/{teacherId}/rights", courseId, teacherId))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void testGetRights_withInternalError_shouldReturnServerError() throws Exception {
        // Arrange
        String teacherId = "teacher123";
        String courseId = "course456";
        Mockito.when(teacherRightsService.getRights(anyString(), anyString()))
                .thenThrow(new EntityNotFoundException("Bad Request"));

        // Act & Assert
        mockMvc.perform(get("/api/v1/courses/{courseId}/teachers/{teacherId}/rights", courseId, teacherId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSetRights_withValidRights_shouldReturnOk() throws Exception {
        // Arrange
        String teacherId = "teacher123";
        String courseId = "course456";
        Set<String> rights = Set.of("VIEW", "EDIT");
        Mockito.when(teacherRightsService.setRights(courseId, teacherId, rights)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(post("/api/v1/courses/{courseId}/teachers/{teacherId}/rights", courseId, teacherId)
                        .contentType("application/json")
                        .content("[\"VIEW\",\"EDIT\"]"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testSetRights_withInternalError_shouldReturnServerError() throws Exception {
        // Arrange
        String teacherId = "teacher123";
        String courseId = "course456";
        Set<String> rights = Set.of("VIEW", "EDIT");
        Mockito.when(teacherRightsService.setRights(anyString(), anyString(), Mockito.anySet()))
                .thenThrow(new RuntimeException("Internal Error"));

        // Act & Assert
        mockMvc.perform(post("/api/v1/courses/{courseId}/teachers/{teacherId}/rights", courseId, teacherId)
                        .contentType("application/json")
                        .content("[\"VIEW\",\"EDIT\"]"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testSetRights_withInvalidRequest_shouldReturnBadRequest() throws Exception {
        // Arrange
        String teacherId = "teacher123";
        String courseId = "course456";

        // Act & Assert
        mockMvc.perform(post("/api/v1/courses/{courseId}/teachers/{teacherId}/rights", courseId, teacherId)
                        .contentType("application/json")
                        .content("\"Invalid_JSON\""))
                .andExpect(status().isBadRequest());
    }


    @Test
    void testHasRights_withValidRequest_shouldReturnTrue() throws Exception {
        // Arrange
        String teacherId = "teacher123";
        String courseId = "course456";
        Set<String> rights = Set.of("VIEW", "EDIT");
        Mockito.when(teacherRightsService.hasRights(courseId, teacherId, rights)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(get("/api/v1/courses/{courseId}/teachers/{teacherId}/rights/has", courseId, teacherId)
                        .param("rights", "VIEW", "EDIT"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testHasRights_withInvalidRights_shouldReturnFalse() throws Exception {
        // Arrange
        String teacherId = "teacher123";
        String courseId = "course456";
        Set<String> rights = Set.of("DELETE");
        Mockito.when(teacherRightsService.hasRights(courseId, teacherId, rights)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(get("/api/v1/courses/{courseId}/teachers/{teacherId}/rights/has", courseId, teacherId)
                        .param("rights", "DELETE"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    void testHasRights_withInternalError_shouldReturnServerError() throws Exception {
        // Arrange
        String teacherId = "teacher123";
        String courseId = "course456";
        Mockito.when(teacherRightsService.hasRights(anyString(), anyString(), Mockito.anySet()))
                .thenThrow(new RuntimeException("Internal Error"));

        // Act & Assert
        mockMvc.perform(get("/api/v1/courses/{courseId}/teachers/{teacherId}/rights/has", courseId, teacherId)
                        .param("rights", "VIEW"))
                .andExpect(status().isInternalServerError());
    }
}