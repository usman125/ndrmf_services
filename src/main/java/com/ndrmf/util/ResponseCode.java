package com.ndrmf.util;

public enum ResponseCode {

    SUCCESS("H006", CommonConstants.SUCCESS_DESC),
    FAILURE("H009", CommonConstants.FAILURE_DESC),

    DUPLICATE_RESOURCE("H011", CommonConstants.DUPLICATE_RESOURCE_DESC),
    INVALID_CLIENT_REQUEST("H012", CommonConstants.INVALID_CLIENT_REQUEST_DESC),

    INVALID_CREDENTIALS("H021", CommonConstants.INVALID_CREDENTIALS_DESC),
    DATA_NOT_FOUND("H022", CommonConstants.DATA_NOT_FOUND_DESC); //INVALID_INFORMATION


    private final String respCode;
    private final String respDesc;

    ResponseCode(String respCode, String respDesc) {
        this.respCode = respCode;
        this.respDesc = respDesc;
    }

    public String getRespCode() {
        return respCode;
    }

    public String getRespDesc() {
        return respDesc;
    }
}
