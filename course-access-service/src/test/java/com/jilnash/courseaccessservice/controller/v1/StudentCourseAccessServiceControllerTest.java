package com.jilnash.courseaccessservice.controller.v1;

import com.jilnash.courseaccessservice.model.StudentCourseAccess;
import com.jilnash.courseaccessservice.service.StudentCourseAccessService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class StudentCourseAccessServiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentCourseAccessService studentCourseAccessService;

    @Test
    public void testGetStudentHasAccess_WhenAccessExists_ShouldReturnTrue() throws Exception {
        String studentId = "student123";
        String courseId = "course456";

        when(studentCourseAccessService.getStudentHasAccess(studentId, courseId)).thenReturn(true);

        mockMvc.perform(get("/api/v1/course-access")
                        .param("studentId", studentId)
                        .param("courseId", courseId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        Mockito.verify(studentCourseAccessService).getStudentHasAccess(studentId, courseId);
    }

    @Test
    public void testGetStudentHasAccess_WhenAccessDoesNotExist_ShouldReturnFalse() throws Exception {
        String studentId = "student123";
        String courseId = "course456";

        when(studentCourseAccessService.getStudentHasAccess(studentId, courseId)).thenReturn(false);

        mockMvc.perform(get("/api/v1/course-access")
                        .param("studentId", studentId)
                        .param("courseId", courseId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));

        Mockito.verify(studentCourseAccessService).getStudentHasAccess(studentId, courseId);
    }

    @Test
    public void testGetStudentHasAccess_WhenMissingParams_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/v1/course-access")
                        .param("studentId", "student123")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void testCreate_WhenSuccessful_ShouldReturnOk() throws Exception {
        String studentId = "student123";
        String courseId = "course456";
        StudentCourseAccess studentCourseAccess = new StudentCourseAccess();

        when(studentCourseAccessService.purchase(Mockito.any())).thenReturn(studentCourseAccess);

        mockMvc.perform(post("/api/v1/course-access")
                        .param("studentId", studentId)
                        .param("courseId", courseId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(studentCourseAccessService).purchase(Mockito.any());
    }

    @Test
    public void testCreate_WhenMissingParams_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/course-access")
                        .param("studentId", "student123")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}