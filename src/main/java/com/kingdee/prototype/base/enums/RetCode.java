package com.kingdee.prototype.base.enums;

public enum RetCode {

    SUCCESS("100", "ok"),

    FAIL("-9999", "fail");

    public String retCode;

    public String message;

    private RetCode(String retCode, String message) {
        this.retCode = retCode;
        this.message = message;
    }
}
