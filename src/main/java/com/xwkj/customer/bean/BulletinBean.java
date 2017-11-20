package com.xwkj.customer.bean;

import com.xwkj.customer.domain.Bulletin;
import org.directwebremoting.annotations.DataTransferObject;

import java.util.Date;

@DataTransferObject
public class BulletinBean {

    private String bid;
    private Date createAt;
    private String content;
    private EmployeeBean employee;

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public EmployeeBean getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeBean employee) {
        this.employee = employee;
    }

    public BulletinBean(Bulletin bulletin) {
        this.bid = bulletin.getBid();
        this.createAt = new Date(bulletin.getCreateAt());
        this.content = bulletin.getContent();
        this.employee = new EmployeeBean(bulletin.getEmployee(), false);
    }

}
