package com.jilnash.hwresponseservicesaga.service;

import com.jilnash.hwresponseservicedto.dto.ResponseCreateDTO;

public interface ResponseServiceSaga {

    void createResponse(ResponseCreateDTO response);

    void updateResponse(ResponseCreateDTO response);

    void softDeleteResponse(String courseId, String teacherId, String responseId);

    void hardDeleteResponse(String courseId, String teacherId, String responseId);

}
