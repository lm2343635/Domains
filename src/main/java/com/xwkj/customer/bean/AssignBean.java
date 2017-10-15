package com.xwkj.customer.bean;

import com.xwkj.customer.domain.Assign;
import org.directwebremoting.annotations.DataTransferObject;

import java.util.Date;

@DataTransferObject
public class AssignBean {

    private boolean r;
    private boolean w;
    private boolean d;
    private boolean assign;
    private Date createAt;
    private CustomerBean customer;

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

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public CustomerBean getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerBean customer) {
        this.customer = customer;
    }

    public AssignBean(boolean r, boolean w, boolean d, boolean assign) {
        this.r = r;
        this.w = w;
        this.d = d;
        this.assign = assign;
    }

    public AssignBean(Assign assign) {
        this.r = assign.getR();
        this.w = assign.getW();
        this.d = assign.getD();
        this .assign = assign.getAssign();
        this.createAt = new Date(assign.getCreateAt());
        this.customer = new CustomerBean(assign.getCustomer(), false);
    }

}
