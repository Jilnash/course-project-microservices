package com.jilnash.hwresponseservice.service;

import com.jilnash.hwresponseservice.model.mongo.HwResponse;

import java.util.Date;
import java.util.List;

public interface HwResponseService {

    List<HwResponse> getResponses(String teacherId, Long homeworkId, Date createdAfter, Date createdBefore);

    HwResponse getResponse(String responseId);

    Boolean createResponse(HwResponse response);

    Boolean updateResponse(HwResponse response);

    Boolean softDeleteResponse(String responseId);

    Boolean hardDeleteResponse(String responseId);
}
