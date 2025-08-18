package com.jilnash.courseservice.service.course;

import com.jilnash.courseservice.history.EntityKey;
import com.jilnash.courseservice.history.EntityValue;
import com.jilnash.courseservice.repo.CourseRepo;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.NoSuchElementException;

@Component
public class CourseServiceRollbackImpl implements CourseServiceRollback {

    private final CourseRepo courseRepo;

    private final Map<EntityKey, EntityValue> entityHistory;

    public CourseServiceRollbackImpl(CourseRepo courseRepo,
                                     Map<EntityKey, EntityValue> entityHistory) {
        this.courseRepo = courseRepo;
        this.entityHistory = entityHistory;
    }

    public <T> T getPreviousValue(String entityId, String fieldName, Class<T> tClass) {
        Object value = entityHistory.get(new EntityKey(entityId, fieldName)).value();
        return tClass.cast(value);
    }

    @Override
    public Boolean createCourseRollback(String createdCourse) {
        return courseRepo.findById(createdCourse)
                .map(course -> {
                    courseRepo.deleteById(createdCourse);
                    return true;
                })
                .orElseThrow(() -> new NoSuchElementException("Course not found with id: " + createdCourse));
    }

    @Override
    public Boolean updateCourseNameRollback(String courseId) {
        String prevName = getPreviousValue(courseId, "name", String.class);
        courseRepo.updateCourseName(courseId, prevName);
        return true;
    }

    @Override
    public Boolean updateCourseDescriptionRollback(String courseId) {
        String prevDescription = getPreviousValue(courseId, "description", String.class);
        courseRepo.updateCourseDescription(courseId, prevDescription);
        return true;
    }

    @Override
    public Boolean updateCourseDurationRollback(String courseId) {
        String prevDuration = getPreviousValue(courseId, "duration", String.class);
        courseRepo.updateCourseDuration(courseId, prevDuration);
        return true;
    }

    @Override
    public Boolean softDeleteRollback(String id) {
        return courseRepo.findById(id).
                map(course -> {
                    course.setDeletedAt(null);
                    courseRepo.save(course);
                    return true;
                })
                .orElseThrow(() -> new NoSuchElementException("Course not found with id: " + id));
    }
}
