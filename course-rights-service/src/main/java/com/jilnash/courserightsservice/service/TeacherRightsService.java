package com.jilnash.courserightsservice.service;

import com.jilnash.courserightsservice.model.TeacherRights;
import com.jilnash.courserightsservice.repo.RightRepository;
import com.jilnash.courserightsservice.repo.TeacherRightsRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * Service class responsible for managing rights of a teacher for a specific course.
 * Provides methods to retrieve, check, and set rights for teachers, as well as creating a course owner.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TeacherRightsService {

    private final TeacherRightsRepo teacherRightsRepo;

    private final RightRepository rightRepo;

    /**
     * Retrieves the set of rights associated with a specific teacher for a given course.
     *
     * @param courseId  the unique identifier of the course for which the teacher's rights are being retrieved
     * @param teacherId the unique identifier of the teacher whose rights are being fetched
     * @return a set of rights associated with the specified teacher for the given course
     */
    public Set<String> getRights(String courseId, String teacherId) {
        return teacherRightsRepo.getTeacherRightsByTeacherIdAndCourseId(teacherId, courseId);
    }

    /**
     * Determines whether a teacher has the specified rights for a given course.
     *
     * @param courseId  the unique identifier of the course for which the rights are being checked
     * @param teacherId the unique identifier of the teacher whose rights are being checked
     * @param rights    the set of rights to verify against the teacher's granted rights
     * @return true if the teacher has all the specified rights for the course, false otherwise
     */
    public Boolean hasRights(String courseId, String teacherId, Set<String> rights) {
        return teacherRightsRepo.getTeacherRightsByTeacherIdAndCourseId(teacherId, courseId).containsAll(rights);
    }

    /**
     * Assigns a set of rights to a teacher for a specific course. All previously assigned rights for the teacher
     * in the course are removed before the new rights are saved. Throws an exception if any of the specified rights
     * are not found.
     *
     * @param courseId the unique identifier of the course for which the rights are being set
     * @param teacherId the unique identifier of the teacher for whom the rights are being set
     * @param rights the set of rights to be assigned to the teacher
     * @return true if the operation is successful
     * @throws EntityNotFoundException if any of the specified rights are not found
     */
    @Transactional
    public Boolean setRights(String courseId, String teacherId, Set<String> rights) {

        if (rights.isEmpty()) {
            log.warn("[SERVICE] No rights provided for teacher: {}, in course: {}", teacherId, courseId);
            throw new IllegalArgumentException("No rights provided");
        }

        log.info("[SERVICE] Setting course rights for teacher");
        log.debug("[SERVICE] Setting rights: {}, for teacher: {}, in course: {}", rights, teacherId, courseId);

        teacherRightsRepo.deleteAllByCourseIdAndTeacherId(courseId, teacherId);
        teacherRightsRepo.saveAll(
                rightRepo.findAllByNameIn(rights)
                        .orElseThrow(() -> new EntityNotFoundException("Rights not found"))
                        .stream()
                        .map(right -> TeacherRights.builder()
                                .courseId(courseId)
                                .teacherId(teacherId)
                                .right(right)
                                .build())
                        .toList()
        );

        return true;
    }

    /**
     * Creates a course owner by assigning all available rights to a teacher for a specific course.
     *
     * @param courseId the unique identifier of the course for which the teacher is being made the owner
     * @param teacherId the unique identifier of the teacher to be assigned as the course owner
     * @return true if the operation is successful
     */
    @Transactional
    public Boolean createCourseOwner(String courseId, String teacherId) {

        log.info("[SERVICE] Creating course owner");
        log.debug("[SERVICE] Creating course owner for course course: {}", courseId);

        teacherRightsRepo.saveAll(
                rightRepo.findAll().stream().map(right ->
                        TeacherRights.builder()
                                .courseId(courseId)
                                .teacherId(teacherId)
                                .right(right)
                                .build()).toList()
        );

        return true;
    }
}
