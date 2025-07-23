package com.jilnash.courseservicedto.dto.task;

import java.util.List;

public record TaskGraph(List<String> taskIds, List<TaskGraphEdge> edges) {
}
