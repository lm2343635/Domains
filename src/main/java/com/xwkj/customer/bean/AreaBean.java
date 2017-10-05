package com.xwkj.customer.bean;

import com.xwkj.customer.domain.Area;
import org.directwebremoting.annotations.DataTransferObject;

import java.util.Date;

@DataTransferObject
public class AreaBean {

    private String aid;
    private String name;
    private Date createAt;
    private int customers;

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
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

    public AreaBean(Area area) {
        this.aid = area.getAid();
        this.name = area.getName();
        this.createAt = new Date(area.getCreateAt());
        this.customers = area.getCustomers();
    }

}
