package com.jilnash.hwresponseservice.service;

import org.springframework.stereotype.Component;

@Component
public interface HwResponseServiceRollback {

    void rollbackCreateResponse(String responseId);

    void rollbackUpdateResponse(String responseId);

    void rollbackSoftDeleteResponse(String responseId);
}
