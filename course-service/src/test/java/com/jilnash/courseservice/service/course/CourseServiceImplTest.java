package com.jilnash.courseservice.service.course;

import com.jilnash.courseservice.client.TeacherRightsGrpcClient;
import com.jilnash.courseservice.dto.course.CourseCreateDTO;
import com.jilnash.courseservice.dto.course.CourseUpdateDTO;
import com.jilnash.courseservice.mapper.CourseMapper;
import com.jilnash.courseservice.model.Course;
import com.jilnash.courseservice.repo.CourseRepo;
import com.jilnash.courseservice.service.courseauthr.CourseAuthorizationService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CourseServiceImplTest {


    @Mock
    private CourseRepo courseRepo;

    @Mock
    private CourseMapper courseMapper;

    @Mock
    private TeacherRightsGrpcClient teacherRightsGrpcClient;

    @Mock
    private CourseAuthorizationService courseAuthrService;

    @InjectMocks
    private CourseServiceImpl courseService;

    CourseServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCourses_givenCourseName_whenGetCourses_thenReturnFilteredCourses() {
        Course course = new Course();
        course.setId("1");
        course.setName("Java Programming");

        when(courseRepo.findAllByNameContaining("Java")).thenReturn(Collections.singletonList(course));

        List<Course> result = courseService.getCourses("Java");

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.getFirst().getName()).isEqualTo("Java Programming");
    }

    @Test
    void testGetCourses_givenEmptyCourseName_whenGetCourses_thenReturnAllCourses() {
        Course course1 = new Course();
        course1.setId("1");
        course1.setName("Java Programming");

        Course course2 = new Course();
        course2.setId("2");
        course2.setName("Python Programming");

        when(courseRepo.findAll()).thenReturn(List.of(course1, course2));

        List<Course> result = courseService.getCourses("");

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
        assertThat(result).extracting(Course::getName)
                .containsExactlyInAnyOrder("Java Programming", "Python Programming");
    }

    @Test
    void testGetCourses_givenNonexistentCourseName_whenGetCourses_thenReturnEmptyList() {
        when(courseRepo.findAllByNameContaining(anyString())).thenReturn(Collections.emptyList());

        List<Course> result = courseService.getCourses("Nonexistent");

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    @Test
    void testGetCourse_givenValidCourseId_whenGetCourse_thenReturnCourse() {
        Course course = new Course();
        course.setId("1");
        course.setName("Java Programming");

        when(courseRepo.findById("1")).thenReturn(java.util.Optional.of(course));

        Course result = courseService.getCourse("1");

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("1");
        assertThat(result.getName()).isEqualTo("Java Programming");
    }

    @Test
    void testGetCourse_givenNonexistentCourseId_whenGetCourse_thenThrowNoSuchElementException() {
        when(courseRepo.findById("99")).thenReturn(java.util.Optional.empty());

        try {
            courseService.getCourse("99");
        } catch (NoSuchElementException e) {
            assertThat(e.getMessage()).isEqualTo("Course not found with id: 99");
        }
    }

    @Test
    void testCreateCourse_givenValidCourseCreateDTO_whenCreate_thenReturnSavedCourse() {
        CourseCreateDTO courseCreateDTO = new CourseCreateDTO();
        courseCreateDTO.setName("Java 101");
        courseCreateDTO.setDescription("Introduction to Java");
        courseCreateDTO.setDuration("10 hours");
        Course savedCourse = new Course();
        savedCourse.setId("1");
        savedCourse.setName("Java 101");

        when(teacherRightsGrpcClient.createCourseOwner(any(), any())).thenReturn(true);
        when(courseMapper.toNode(courseCreateDTO)).thenReturn(savedCourse);
        when(courseRepo.save(savedCourse)).thenReturn(savedCourse);

        Course result = courseService.create(courseCreateDTO);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("1");
        assertThat(result.getName()).isEqualTo("Java 101");
    }

    @Test
    void testCreateCourse_givenFailureToSetCourseOwner_whenCreate_thenThrowRuntimeException() {
        CourseCreateDTO courseCreateDTO = new CourseCreateDTO();

        when(teacherRightsGrpcClient.createCourseOwner(any(), any())).thenReturn(false);

        try {
            courseService.create(courseCreateDTO);
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isEqualTo("Failed to set course owner");
        }
    }

    @Test
    void testUpdateCourse_givenValidCourseUpdateDTO_whenUpdate_thenReturnUpdatedCourse() {
        CourseUpdateDTO courseUpdateDTO = new CourseUpdateDTO();
        courseUpdateDTO.setId("1");
        courseUpdateDTO.setName("Updated Java 101");
        courseUpdateDTO.setDescription("Updated Description");
        courseUpdateDTO.setDuration("15 hours");
        courseUpdateDTO.setHwPostingDayInterval(5);

        Course updatedCourse = new Course();
        updatedCourse.setId("1");
        updatedCourse.setName("Updated Java 101");
        updatedCourse.setDescription("Updated Description");
        updatedCourse.setDuration("15 hours");
        updatedCourse.setHwPostingDayInterval(5);

        doNothing().when(courseAuthrService).validateTeacherCourseRights("1", null, List.of("UPDATE"));
        when(courseRepo.existsById("1")).thenReturn(true);
        when(courseMapper.toNode(courseUpdateDTO)).thenReturn(updatedCourse);
        when(courseRepo.save(updatedCourse)).thenReturn(updatedCourse);

        Course result = courseService.update(courseUpdateDTO);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("1");
        assertThat(result.getName()).isEqualTo("Updated Java 101");
        assertThat(result.getDescription()).isEqualTo("Updated Description");
        assertThat(result.getDuration()).isEqualTo("15 hours");
        assertThat(result.getHwPostingDayInterval()).isEqualTo(5);
    }

    @Test
    void testUpdateCourse_givenNonexistentCourseId_whenUpdate_thenThrowNoSuchElementException() {
        CourseUpdateDTO courseUpdateDTO = new CourseUpdateDTO();
        courseUpdateDTO.setId("99");

        when(courseRepo.existsById("99")).thenReturn(false);

        try {
            courseService.update(courseUpdateDTO);
        } catch (NoSuchElementException e) {
            assertThat(e.getMessage()).isEqualTo("Course not found with id: 99");
        }
    }

    @Test
    void testUpdateCourse_givenUnauthorizedTeacher_whenUpdate_thenThrowRuntimeException() {
        CourseUpdateDTO courseUpdateDTO = new CourseUpdateDTO();
        courseUpdateDTO.setId("1");

        doThrow(new RuntimeException("Unauthorized teacher")).when(courseAuthrService)
                .validateTeacherCourseRights("1", null, List.of("UPDATE"));

        try {
            courseService.update(courseUpdateDTO);
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isEqualTo("Unauthorized teacher");
        }
    }
}