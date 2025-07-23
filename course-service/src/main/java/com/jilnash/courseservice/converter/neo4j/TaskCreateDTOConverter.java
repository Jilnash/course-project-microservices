package com.jilnash.courseservice.converter.neo4j;

import com.jilnash.courseservicedto.dto.task.TaskCreateDTO;
import org.neo4j.driver.Value;
import org.neo4j.driver.Values;
import org.springframework.core.convert.converter.Converter;

public class TaskCreateDTOConverter implements Converter<TaskCreateDTO, Value> {

    @Override
    public Value convert(TaskCreateDTO source) {
        return Values.parameters(
                "taskId", source.getTaskId(),
                "title", source.getTitle(),
                "description", source.getDescription(),
                "videoFileName", source.getVideoFileName(),
                "isPublic", source.getIsPublic(),
                "courseId", source.getCourseId(),
                "moduleId", source.getModuleId(),
                "prerequisiteTasksIds", source.getPrerequisiteTasksIds(),
                "successorTasksIds", source.getSuccessorTasksIds()
        );
    }
}
