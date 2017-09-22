package com.xwkj.customer.bean;

import org.directwebremoting.annotations.DataTransferObject;

@DataTransferObject
public class Result {

    private boolean session;
    private boolean privilege;
    private boolean success;
    private Object data = null;

    public boolean isSession() {
        return session;
    }

    public void setSession(boolean session) {
        this.session = session;
    }

    public boolean isPrivilege() {
        return privilege;
    }

    public void setPrivilege(boolean privilege) {
        this.privilege = privilege;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Result(boolean session, boolean privilege) {
        this.session = session;
        this.privilege = privilege;
    }

    public Result(boolean session, boolean privilege, Object data) {
        this.session = session;
        this.privilege = privilege;
        this.data = data;
    }

    public static Result NoSession() {
        return new Result(false, false);
    }

    public static Result NoPrivilege() {
        return new Result(true, false);
    }

    public static Result WithData(Object data) {
        return new Result(true, true, data);
    }

}
