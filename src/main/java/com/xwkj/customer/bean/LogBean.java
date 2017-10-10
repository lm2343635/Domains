package com.xwkj.customer.bean;

import com.xwkj.customer.domain.Log;
import org.directwebremoting.annotations.DataTransferObject;

import java.util.Date;

@DataTransferObject
public class LogBean {

    private String lid;
    private Date createAt;
    private Date updateAt;
    private String title;
    private String content;
    private CustomerBean customer;
    private EmployeeBean employee;

    public String getLid() {
        return lid;
    }

    public void setLid(String lid) {
        this.lid = lid;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public CustomerBean getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerBean customer) {
        this.customer = customer;
    }

    public EmployeeBean getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeBean employee) {
        this.employee = employee;
    }

    public LogBean(Log log, boolean full) {
        this.lid = log.getLid();
        this.createAt = new Date(log.getCreateAt());
        this.updateAt = new Date(log.getUpdateAt());
        this.title = log.getTitle();
        this.customer = new CustomerBean(log.getCustomer(), false);
        this.employee = new EmployeeBean(log.getEmployee(), false);
        if (full) {
            this.content = log.getContent();
        }
    }

}
