package com.xwkj.customer.bean;

import com.xwkj.customer.domain.Industry;
import org.directwebremoting.annotations.DataTransferObject;

import java.util.Date;

@DataTransferObject
public class IndustryBean {

    private String iid;
    private String name;
    private Date createAt;
    private int customers;

    public String getIid() {
        return iid;
    }

    public void setIid(String iid) {
        this.iid = iid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public int getCustomers() {
        return customers;
    }

    public void setCustomers(int customers) {
        this.customers = customers;
    }

    public IndustryBean(Industry industry) {
        this.iid = industry.getIid();
        this.name = industry.getName();
        this.createAt = new Date(industry.getCreateAt());
        this.customers = industry.getCustomers();
    }

}
