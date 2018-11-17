package com.xwkj.customer.controller.common;

public enum ErrorCode {

    ErrorNoSession(801, "Session is none or timeout."),
    ErrorNoPrivilge(802, "No privilege to invoke this method."),
    ErrorObjecId(803, "Object cannot be found by this cid."),

    // User
    ErrorIllegalIDeviceOS(1011, "Device OS should be iOS or Android."),
    ErrorUserNameNotExist(1012, "This user name is not exsit."),
    ErrorPasswordWrong(1013, "Password is wrong.");

    public int code;
    public String message;

    private ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
