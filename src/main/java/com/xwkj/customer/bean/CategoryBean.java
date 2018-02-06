package com.xwkj.customer.bean;

import com.xwkj.customer.domain.Category;
import org.directwebremoting.annotations.DataTransferObject;

import java.util.Date;

@DataTransferObject
public class CategoryBean {

    private String cid;
    private String name;
    private Date createAt;
    private int reports;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
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

    public int getReports() {
        return reports;
    }

    public void setReports(int reports) {
        this.reports = reports;
    }

    public CategoryBean(Category category) {
        this.cid = category.getCid();
        this.name = category.getName();
        this.createAt = new Date(category.getCreateAt());
        this.reports = category.getReports();
    }

}
