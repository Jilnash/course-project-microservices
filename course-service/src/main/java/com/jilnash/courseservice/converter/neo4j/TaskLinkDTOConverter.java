package com.jilnash.courseservice.converter.neo4j;

import com.jilnash.courseservice.dto.task.TaskLinkDTO;
import org.neo4j.driver.Value;
import org.neo4j.driver.Values;
import org.springframework.core.convert.converter.Converter;

public class TaskLinkDTOConverter implements Converter<TaskLinkDTO, Value> {

    @Override
    public Value convert(TaskLinkDTO source) {
        return Values.parameters(
                "fromTaskId", source.from(),
                "toTaskId", source.to()
        );
    }
}
