package com.jilnash.courseservice.converter.neo4j;

import com.jilnash.courseservice.dto.task.TaskCreateDTO;
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
                "videoLink", source.getVideoLink(),
                "audioRequired", source.getAudioRequired(),
                "videoRequired", source.getVideoRequired(),
                "courseId", source.getCourseId(),
                "moduleId", source.getModuleId(),
                "prerequisiteTasksIds", source.getPrerequisiteTasksIds(),
                "successorTasksIds", source.getSuccessorTasksIds()
        );
    }
}
