package com.jilnash.hwresponseservice.service;

public interface HwResponseServiceRollback {

    void rollbackCreateResponse(String responseId);

    void rollbackUpdateResponse(String responseId);

    void rollbackSoftDeleteResponse(String responseId);
}
