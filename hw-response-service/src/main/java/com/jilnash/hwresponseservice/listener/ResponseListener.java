package com.jilnash.hwresponseservice.listener;

import com.jilnash.hwresponseservice.mapper.HwResponseMapper;
import com.jilnash.hwresponseservice.service.HwResponseService;
import com.jilnash.hwresponseservicedto.dto.ResponseCreateDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ResponseListener {

    private final HwResponseService responseService;

    private final HwResponseMapper responseMapper;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public ResponseListener(HwResponseService responseService,
                            HwResponseMapper responseMapper,
                            KafkaTemplate<String, String> kafkaTemplate) {
        this.responseService = responseService;
        this.responseMapper = responseMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "response-create-topic", groupId = "hw-response-group")
    public void listenCreateResponse(ResponseCreateDTO createDTO) {
        try {
            responseService.createResponse(responseMapper.toEntity(createDTO));
        } catch (Exception e) {
            kafkaTemplate.send("rollback-topic", createDTO.getTransactionId());
        }
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
