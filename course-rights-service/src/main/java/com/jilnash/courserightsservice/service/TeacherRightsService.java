package com.jilnash.courserightsservice.service;

import com.jilnash.courserightsservice.model.TeacherRights;
import com.jilnash.courserightsservice.repo.RightRepository;
import com.jilnash.courserightsservice.repo.TeacherRightsRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

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

        teacherRightsRepo.deleteAllByCourseIdAndTeacherId(courseId, teacherId);
        teacherRightsRepo.saveAll(
                rightRepo.findAllByNameIn(rights)
                        .orElseThrow(() -> new RuntimeException("Rights not found"))
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
