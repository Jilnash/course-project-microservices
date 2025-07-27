package com.jilnash.hwresponseservice.model.mongo;

import com.jilnash.hwresponseservicedto.dto.Comments;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@Document(collection = "hwResponses")
public class HwResponse {

    @MongoId
    private String id;

    private String homeworkId;

    private String teacherId;

    private Boolean isCorrect;

    private List<Comments> comments;

    private Date createdAt;

    private Date updatedAt;

    private Date deletedAt;
}
