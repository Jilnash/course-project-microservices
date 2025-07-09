package com.jilnash.courseservice.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.jilnash.courseservice.model.Task;

import java.io.IOException;
import java.util.Set;

public class TaskListSerializer extends JsonSerializer<Set<Task>> {

    @Override
    public void serialize(Set<Task> tasks, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        if (tasks == null) {
            jsonGenerator.writeNull();
            return;
        }

        jsonGenerator.writeObject(tasks.stream().map(Task::getId).toList());
    }
}
