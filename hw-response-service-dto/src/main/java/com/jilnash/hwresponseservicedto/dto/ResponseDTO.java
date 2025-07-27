package com.jilnash.hwresponseservicedto.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ResponseDTO {
    private List<Comments> comments;
    private Date createdAt;
    private Date updatedAt;
}
