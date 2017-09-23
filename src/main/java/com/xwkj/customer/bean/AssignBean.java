package com.xwkj.customer.bean;

import com.xwkj.customer.domain.Assign;
import org.directwebremoting.annotations.DataTransferObject;

@DataTransferObject
public class AssignBean {

    private boolean r;
    private boolean w;
    private boolean d;
    private boolean assign;

    public boolean isR() {
        return r;
    }

    public void setR(boolean r) {
        this.r = r;
    }

    public boolean isW() {
        return w;
    }

    public void setW(boolean w) {
        this.w = w;
    }

    public boolean isD() {
        return d;
    }

    public void setD(boolean d) {
        this.d = d;
    }

    public boolean isAssign() {
        return assign;
    }

    public void setAssign(boolean assign) {
        this.assign = assign;
    }

    public AssignBean(boolean r, boolean w, boolean d, boolean assign) {
        this.r = r;
        this.w = w;
        this.d = d;
        this.assign = assign;
    }

}
