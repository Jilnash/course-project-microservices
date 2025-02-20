package com.jilnash.courseservice.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.jilnash.courseservice.model.Task;

import java.io.IOException;
import java.util.List;

public class TaskListSerializer extends JsonSerializer<List<Task>> {

    @Override
    public void serialize(List<Task> tasks, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        if (tasks == null) {
            jsonGenerator.writeNull();
            return;
        }

        jsonGenerator.writeObject(tasks.stream().map(Task::getId).toList());
    }
}
