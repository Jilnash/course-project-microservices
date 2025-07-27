package com.jilnash.hwresponseservice.listener;

import com.jilnash.hwresponseservice.mapper.HwResponseMapper;
import com.jilnash.hwresponseservice.service.HwResponseService;
import com.jilnash.hwresponseservicedto.dto.ResponseCreateDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ResponseListener {

    private final HwResponseService responseService;

    private final HwResponseMapper responseMapper;

    public ResponseListener(HwResponseService responseService, HwResponseMapper responseMapper) {
        this.responseService = responseService;
        this.responseMapper = responseMapper;
    }

    @KafkaListener(topics = "response-create-topic", groupId = "hw-response-group")
    public void listenCreateResponse(ResponseCreateDTO createDTO) {
        responseService.createResponse(responseMapper.toEntity(createDTO));
    }

    @KafkaListener(topics = "response-update-topic", groupId = "hw-response-group")
    public void listenUpdateResponse(ResponseCreateDTO updateDTO) {
        responseService.updateResponse(responseMapper.toEntity(updateDTO));
    }

    @KafkaListener(topics = "response-soft-delete-topic", groupId = "hw-response-group")
    public void listenSoftDeleteResponse(String id) {
        responseService.softDeleteResponse(id);
    }

    @KafkaListener(topics = "response-hard-delete-topic", groupId = "hw-response-group")
    public void listenHardDeleteResponse(String id) {
        responseService.hardDeleteResponse(id);
    }
}
