package com.jilnash.courseservice.aspect;

import com.jilnash.courseservice.history.EntityKey;
import com.jilnash.courseservice.history.EntityValue;
import com.jilnash.courseservice.service.course.CourseService;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Map;

@Aspect
@Component
public class CourseServiceAspect {

    private final Map<EntityKey, EntityValue> entityHistory;
    private final CourseService courseService;

    public CourseServiceAspect(Map<EntityKey, EntityValue> entityHistory, CourseService courseService) {
        this.entityHistory = entityHistory;
        this.courseService = courseService;
    }

    @Before(
            value = "execution(* com.jilnash.courseservice.service.course.CourseServiceImpl.updateCourseName(..))" +
                    " && args(courseId, *)",
            argNames = "courseId"
    )
    public void beforeUpdateCourseName(String courseId) {
        String name = courseService.getCourseName(courseId);
        entityHistory.put(new EntityKey(courseId, "name"), new EntityValue(name));
    }

    @Before(
            value = "execution(* com.jilnash.courseservice.service.course.CourseServiceImpl.updateCourseDescription(..))" +
                    " && args(courseId, *)",
            argNames = "courseId"
    )
    public void beforeUpdateCourseDescription(String courseId) {
        String description = courseService.getCourseDescription(courseId);
        entityHistory.put(new EntityKey(courseId, "description"), new EntityValue(description));
    }

    @Before(
            value = "execution(* com.jilnash.courseservice.service.course.CourseServiceImpl.updateCourseDuration(..))" +
                    " && args(courseId, *)",
            argNames = "courseId"
    )
    public void beforeUpdateCourseDuration(String courseId) {
        String duration = courseService.getCourseDuration(courseId);
        entityHistory.put(new EntityKey(courseId, "duration"), new EntityValue(duration));
    }
}
