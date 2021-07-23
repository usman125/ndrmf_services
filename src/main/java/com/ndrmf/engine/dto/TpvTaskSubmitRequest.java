package com.ndrmf.engine.dto;
import java.util.UUID;

public class TpvTaskSubmitRequest {
    private String data;
    private UUID requestId;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public UUID getRequestId() {
        return requestId;
    }

    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
    }
}
