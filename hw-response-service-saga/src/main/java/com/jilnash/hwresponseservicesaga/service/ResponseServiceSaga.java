package com.jilnash.hwresponseservicesaga.service;

import com.jilnash.hwresponseservicedto.dto.ResponseCreateDTO;
import org.springframework.stereotype.Service;

@Service
public interface ResponseServiceSaga {

    void createResponse(ResponseCreateDTO response);

    void updateResponse(ResponseCreateDTO response);

    void softDeleteResponse(String teacherId, String responseId);

    void hardDeleteResponse(String teacherId, String responseId);

}
