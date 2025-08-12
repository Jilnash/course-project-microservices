package com.jilnash.hwresponseservicedto.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import java.util.List;

@Getter
@Setter
public class ResponseCreateDTO {

    private String transactionId;

    private String id;
    private String teacherId;
    private String hwId;
    private List<@Valid Comments> comments;
    private Boolean isCorrect;

    private String courseId;
    private String taskId;
    private String studentId;
}
