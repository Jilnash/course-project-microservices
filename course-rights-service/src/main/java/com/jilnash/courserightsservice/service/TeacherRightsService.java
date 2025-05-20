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

@Slf4j
@Service
@RequiredArgsConstructor
public class TeacherRightsService {

    private final TeacherRightsRepo teacherRightsRepo;

    private final RightRepository rightRepo;

    public Set<String> getRights(String courseId, String teacherId) {
        return teacherRightsRepo.getTeacherRightsByTeacherIdAndCourseId(teacherId, courseId);
    }

    public Boolean hasRights(String courseId, String teacherId, Set<String> rights) {
        return teacherRightsRepo.getTeacherRightsByTeacherIdAndCourseId(teacherId, courseId).containsAll(rights);
    }

    @Transactional
    public Boolean setRights(String courseId, String teacherId, Set<String> rights) {

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
