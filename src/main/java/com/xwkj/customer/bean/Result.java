package com.xwkj.customer.bean;

import org.directwebremoting.annotations.DataTransferObject;

@DataTransferObject
public class Result {

    private boolean session;
    private boolean success;
    private Object data = null;

    public boolean isSession() {
        return session;
    }

    public void setSession(boolean session) {
        this.session = session;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Result(boolean session, boolean success) {
        this.session = session;
        this.success = success;
    }

    public Result(boolean session, boolean success, Object data) {
        this.session = session;
        this.success = success;
        this.data = data;
    }

    public static Result NoSession() {
        return new Result(false, false);
    }

}
