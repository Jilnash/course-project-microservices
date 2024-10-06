package com.jilnash.hwresponseservice.service;

import com.jilnash.hwresponseservice.model.HwResponse;

import java.util.Date;
import java.util.List;

public interface HwResponseService {

    List<HwResponse> getResponses(String teacherId, Long homeworkId, Date createdAfter, Date createdBefore);

    HwResponse getResponse(Long id);

    HwResponse createResponse(HwResponse response);

    HwResponse updateResponse(HwResponse response);
}
